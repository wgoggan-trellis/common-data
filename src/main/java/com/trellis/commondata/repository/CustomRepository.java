package com.trellis.commondata.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trellis.commondata.model.CommonDataModel;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface CustomRepository {

   Logger logger = LoggerFactory.getLogger(CustomRepository.class);
   String PREDICATES = "predicates";
   String IN_PREDICATES = "in_predicates";

   default <T extends CommonDataModel> Page<T> filterByCriteria(EntityManager em, List<Filter> filters, List<String> groupByColumns,
                                                                Pageable page, Class clazz) throws IllegalAccessException, InstantiationException {
      boolean hasGroupBy = groupByColumns != null && !groupByColumns.isEmpty();
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery queryCriteria;
      if (hasGroupBy) {
         queryCriteria = criteriaBuilder.createTupleQuery();
      }
      else {
         queryCriteria = criteriaBuilder.createQuery(clazz);
      }
      Root<T> root = queryCriteria.from(clazz);
      Map<String, List<Predicate>> predicates = createFilerPredicates(criteriaBuilder, filters, root);
      Predicate[] predArray = new Predicate[predicates.get(PREDICATES).size()];
      Predicate[] inPredArray = new Predicate[predicates.get(IN_PREDICATES).size()];
      predicates.get(PREDICATES).toArray(predArray);
      predicates.get(IN_PREDICATES).toArray(inPredArray);
      queryCriteria.where(predArray);
      if (inPredArray.length > 0) {
         queryCriteria.where(criteriaBuilder.or(inPredArray));
      }
      if (!hasGroupBy || groupByColumns.contains(page.getSort().toString())) {
         queryCriteria.orderBy(QueryUtils.toOrders(page.getSort(), root, criteriaBuilder));
      }

      List<Selection<?>> groupBySelections = Lists.newArrayList();
      if (hasGroupBy) {
         addGroupBySelections(groupByColumns, root, clazz, criteriaBuilder, queryCriteria, groupBySelections);
      }

      long totalRows = ((BigDecimal) createNativeCountQuery(em, queryCriteria).getSingleResult()).longValue();

      TypedQuery<T> query = em.createQuery(queryCriteria);
      query.setFirstResult(page.getPageNumber() * page.getPageSize());
      query.setMaxResults(page.getPageSize());

      List results = query.getResultList();
      if (hasGroupBy) {
         results = convertToModel(results, clazz, groupBySelections);
      }
      return new PageImpl<>(results, page, totalRows);
   }

   private Map<String, List<Predicate>> createFilerPredicates(CriteriaBuilder criteriaBuilder, List<Filter> filters, Root root) {
      List<Predicate> predicates = Lists.newArrayList();
      List<Predicate> inPredicates = Lists.newArrayList();
      for (Filter filter : filters) {
         switch (filter.getOperator()) {
            case EQUALS:
               predicates.add(criteriaBuilder.equal(getPath(root, filter.getField()),
                     castToRequiredType(getPath(root, filter.getField()).getJavaType(), filter.getValue())));
               break;
            case NOT_EQ:
               predicates.add(criteriaBuilder.notEqual(getPath(root, filter.getField()),
                     castToRequiredType(getPath(root, filter.getField()).getJavaType(), filter.getValue())));
               break;
            case GREATER_THAN:
               predicates.add(criteriaBuilder.gt(getPath(root, filter.getField()),
                     (Number) castToRequiredType(getPath(root, filter.getField()).getJavaType(), filter.getValue())));
               break;
            case LESS_THAN:
               predicates.add(criteriaBuilder.lt(getPath(root, filter.getField()),
                     (Number) castToRequiredType(getPath(root, filter.getField()).getJavaType(), filter.getValue())));
               break;
            case LIKE:
               predicates.add(criteriaBuilder.like(getPath(root, filter.getField()), "%" + filter.getValue() + "%"));
               break;
            case NOT_LIKE:
               predicates.add(criteriaBuilder.notLike(getPath(root, filter.getField()), "%" + filter.getValue() + "%"));
               break;
            case STARTS_WITH:
               predicates.add(criteriaBuilder.like(getPath(root, filter.getField()), filter.getValue() + "%"));
               break;
            case ENDS_WITH:
               predicates.add(criteriaBuilder.like(getPath(root, filter.getField()), "%" + filter.getValue()));
               break;
            case IN:
               for (String value : filter.getValues()) {
                  inPredicates.add(criteriaBuilder.equal(getPath(root, filter.getField()),
                        castToRequiredType(getPath(root, filter.getField()).getJavaType(), value)));
               }
               break;
            default:
               throw new RuntimeException("Operation not supported yet");
         }
      }
      Map bothPredicates = Maps.newHashMap();
      bothPredicates.put(PREDICATES, predicates);
      bothPredicates.put(IN_PREDICATES, inPredicates);
      return bothPredicates;
   }

   private Path getPath(Root root, String attributeName) {
      Path path = root;
      for (String part : attributeName.split("\\.")) {
         path = path.get(part);
      }
      return path;
   }

   private Object castToRequiredType(Class fieldType, String value) {
      if (fieldType.isAssignableFrom(Double.class)) {
         return Double.valueOf(value);
      }
      else if (fieldType.isAssignableFrom(Integer.class)) {
         return Integer.valueOf(value);
      }
      else if (fieldType.isAssignableFrom(Long.class)) {
         return Long.valueOf(value);
      }
      else if (fieldType.isAssignableFrom(String.class)) {
         return value;
      }
      else if (Enum.class.isAssignableFrom(fieldType)) {
         return Enum.valueOf(fieldType, value);
      }
      return null;
   }

   private <T extends CommonDataModel> List<Expression<?>> addGroupBySelections(List<String> groupByColumns, Root root, Class clazz,
         CriteriaBuilder criteriaBuilder, CriteriaQuery queryCriteria, List<Selection<?>> groupBySelections) {
      List<Expression<?>> groupByExpressions = groupByColumns.stream().map(c -> (Path<T>) getPath(root, c))
            .collect(Collectors.toList());
      groupBySelections.addAll(groupByExpressions);
      Field[] fields = clazz.getDeclaredFields();
      for (Field field : fields) {
         if (field.isAnnotationPresent(CommonDataModel.GroupSum.class)) {
            groupBySelections.add(criteriaBuilder.sum(getPath(root, field.getName())));
         }
      }
      queryCriteria.groupBy(groupByExpressions);
      queryCriteria.multiselect(groupBySelections);

      return groupByExpressions;
   }

   private <T extends CommonDataModel> List convertToModel(List results, Class clazz,List<Selection<?>> groupBySelections)
         throws IllegalAccessException, InstantiationException {
      List converted = Lists.newArrayList();
      for (int i = 0; i < results.size(); i++) {
         T modelObj = (T) clazz.newInstance();
         Tuple casted = (Tuple) results.get(i);
         for (int j = 0; j < casted.getElements().size(); j++) {
            Selection<?> groupSelection = groupBySelections.get(j);
            if (groupSelection instanceof AggregationFunction) {
               groupSelection = ((AggregationFunction<?>) groupSelection).getArgumentExpressions().get(0);
            }
            String setMethodName = "set" + StringUtils.capitalize(((SingularAttributePath) groupSelection).getAttribute().getName());
            try {
               Method method = modelObj.getClass().getMethod(setMethodName, groupSelection.getJavaType());
               method.invoke(modelObj, casted.get(j));
            } catch (NoSuchMethodException e) {
               logger.error("Method " + setMethodName + " not found", e);
            } catch (InvocationTargetException e) {
               logger.error("Method " + setMethodName + " could not be invoked", e);
            }
         }
         converted.add(modelObj);
      }
      return converted;
   }

   private Query createNativeCountQuery(EntityManager em, CriteriaQuery<?> criteriaQuery) {
      Query<?> hibernateQuery = em.createQuery(criteriaQuery).unwrap(Query.class);
      String hqlQuery = hibernateQuery.getQueryString();

      QueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
      QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator(hqlQuery, hqlQuery,
            Collections.emptyMap(), em.getEntityManagerFactory().unwrap(SessionFactoryImplementor.class), null
      );
      queryTranslator.compile(Collections.emptyMap(), false);

      String sqlCountQueryTemplate = "select count(*) from (%s)";
      String sqlCountQuery = String.format(sqlCountQueryTemplate, queryTranslator.getSQLString());

      Query nativeCountQuery = (Query) em.createNativeQuery(sqlCountQuery);

      Map<Integer, Object> positionalParamBindings = getPositionalParamBindingsFromNamedParams(hibernateQuery);
      positionalParamBindings.forEach(nativeCountQuery::setParameter);

      return nativeCountQuery;
   }

   private Map<Integer, Object> getPositionalParamBindingsFromNamedParams(Query<?> hibernateQuery) {
      Map<Integer, Object> bindings = Maps.newHashMap();
      for (var namedParam : hibernateQuery.getParameterMetadata().getNamedParameters()) {
         for (int location : namedParam.getSourceLocations()) {
            bindings.put(location + 1, hibernateQuery.getParameterValue(namedParam.getName()));
         }
      }
      return bindings;
   }
}
