package com.aist.common.rapidQuery.scriptParse;

import com.aist.common.exception.BusinessException;
import com.aist.common.utils.SpringUtils;
import com.aist.uam.userorg.remote.vo.User;
import ognl.Ognl;
import ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: ExpressionEvaluator.java, v 0.1 2016年5月4日 下午3:43:57 ouyq Exp $
 */
public class ExpressionEvaluator {
    private static final Logger     LOGGER = LoggerFactory.getLogger(ExpressionEvaluator.class);
    private static ExpressionParser parser = new SpelExpressionParser();
    private static StandardEvaluationContext context =new StandardEvaluationContext();
    
    static{
        context.setBeanResolver(new BeanFactoryResolver(SpringUtils.getApplicationContext()));
    }

    public static boolean spelEvaluteBoolean(String expression, Map<String, Object> parammap) {
        Expression exp = parser.parseExpression(expression);
        if (parammap != null && !parammap.isEmpty()) {
            context.setVariables(parammap);
        }
        Boolean value = null;
        value = exp.getValue(context, Boolean.class);
        return value;
    }

    public static Object spelEvalute(String expression, Map<String, Object> parammap) {
        Expression exp = parser.parseExpression(expression);
        if (parammap != null && !parammap.isEmpty()) {
            context.setVariables(parammap);
        }
        Object value = exp.getValue(context);
        return value;
    }

    public static boolean evaluateBoolean(String expression, Object parameterObject) {
        Object value = null;
        try {
            value = Ognl.getValue(expression, parameterObject);
        } catch (OgnlException e) {
            LOGGER.error("表达式：" + expression + "解析出错", e);
        }
        if (value instanceof Boolean)
            return (Boolean) value;
        if (value instanceof Number)
            return !new BigDecimal(String.valueOf(value)).equals(BigDecimal.ZERO);
        return value != null;
    }

    public Iterable<?> evaluateIterable(String expression, Map<String, Object> paramMap) {
        Object value = paramMap.get(expression);
        if (value == null)
            throw new BusinessException("foreach 标签 collection 表达式：" + expression + " 无值. queryId:"
                                        + paramMap.get("queryId"));
        if (value instanceof Iterable)
            return (Iterable<?>) value;
        if (value.getClass().isArray()) {
            int size = Array.getLength(value);
            List<Object> answer = new ArrayList<Object>();
            for (int i = 0; i < size; i++) {
                Object o = Array.get(value, i);
                answer.add(o);
            }
            return answer;
        }
        if (value instanceof Map) {
            return ((Map) value).entrySet();
        }
        throw new BusinessException("foreach 标签  collection 表达式：" + expression + " 不能遍历. queryId:"
                                    + paramMap.get("queryId"));
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("SYS_USER_LOGIN_NAME", "123");
        map.put("abc", "123");
        //        Boolean bool = evaluteBooleanSpel("#abd",map);
        //        System.out.println(bool);

        ExpressionParser parser = new SpelExpressionParser();
        User tesla = new User();
        tesla.setUsername("张三");
        StandardEvaluationContext context = new StandardEvaluationContext();
        //        for (String key : map.keySet()) {
        //            context.setVariable(key, map.get(key));
        //        }
        //        context.setRootObject(map);
        //        context.setVariable("SYS_USER_LOGIN_NAME", "Mike Tesla");

        Object bool = parser.parseExpression("#SYS_USER_LOGIN_NAME != null").getValue(context);
        System.out.println(bool);// "Mike Tesla"
    }
}
