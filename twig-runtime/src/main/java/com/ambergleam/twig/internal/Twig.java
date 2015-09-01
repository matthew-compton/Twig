package com.ambergleam.twig.internal;

import android.os.Looper;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Aspect
public class Twig {

    @Pointcut("within(@com.ambergleam.twig.Twig *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(* *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(*.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.ambergleam.twig.Twig * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }

    @Pointcut("execution(@com.ambergleam.twig.Twig *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    }

    @Around("method() || constructor()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);

        exitMethod(joinPoint, result, lengthMillis);

        return result;
    }

    private static void enterMethod(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();
        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(Strings.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        Log.v(asTag(cls), generateInsult());
        Log.v(asTag(cls), builder.toString());
    }

    private static void exitMethod(JoinPoint joinPoint, Object result, long lengthMillis) {
        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        String methodName = signature.getName();
        boolean hasReturnType = signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }

        Log.v(asTag(cls), builder.toString());
    }

    private static String generateInsult() {
        Random r = new Random();
        int i = r.nextInt(10);
        String insult;
        switch (i) {
            case 0:
                insult = "I bet you have a great personality.";
                break;
            case 1:
                insult = "Good morning, Mr. President.";
                break;
            case 2:
                insult = "Your birth certificate is an apology from the condom factory.";
                break;
            case 3:
                insult = "You must have been born on a highway, because that's where most accidents happen.";
                break;
            case 4:
                insult = "It's better to let someone think you're an idiot than to type some code and prove it.";
                break;
            case 5:
                insult = "I’m jealous of all the people that haven't met you!";
                break;
            case 6:
                insult = "You carry your weight well.";
                break;
            case 7:
                insult = "It looks like your face caught on fire and someone tried to put it out with a fork.";
                break;
            case 8:
                insult = "You probably like enums.";
                break;
            case 9:
                insult = "You're so ugly Hello Kitty said goodbye to you.";
                break;
            default:
                insult = "I really messed up here.";
                break;
        }
        return insult;
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }
}
