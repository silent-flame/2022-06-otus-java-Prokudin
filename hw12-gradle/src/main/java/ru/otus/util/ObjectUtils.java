package ru.otus.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ObjectUtils {
    public static <ARG, RESULT> RESULT let(ARG arg, Function<ARG, RESULT> function) {
        if (arg == null) {
            return null;
        }
        return function.apply(arg);
    }

    public static <ARG, RESULT> List<RESULT> map(List<ARG> argList, Function<ARG, RESULT> function) {
        if (argList == null) {
            return Collections.emptyList();
        }
        List<RESULT> resultList = new ArrayList<>(argList.size());
        for (var arg : argList) {
            resultList.add(function.apply(arg));
        }
        return resultList;
    }
}