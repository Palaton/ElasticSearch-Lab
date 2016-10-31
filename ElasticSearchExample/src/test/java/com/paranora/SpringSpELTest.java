package com.paranora;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by yangqun on 2016/10/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = {"classpath:/spring-context-test.xml"})
public class SpringSpELTest {

    @Test
    public void test(){
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("#itemId == 101");
        StandardEvaluationContext context = new StandardEvaluationContext();

        context.setVariables(new HashMap<String, Object>() {{
            put("itemId",101);
        }});

        Object result=expression.getValue(context);

        System.out.println("spring spel test.");
    }
}
