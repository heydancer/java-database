package com.digdes.school;

import java.util.ArrayList;
import java.util.List;

public final class Formatter {
    private static final String PATTERN = "\\s*(=|!=|<=|>=|<|>)\\s*";
    private static final String SET_CLAUSE_PATTERN = "'|\\s";

    private Formatter() {
    }

    public static List<String> conditionsParser(String request) {
        List<String> res = new ArrayList<>();

        String elements = request.replaceAll("'", "");
        String[] tokens = elements.split(" ");

        for (String token : tokens) {
            String[] tok = token.split(PATTERN);
            if (tok.length == 2) {
                if (token.contains("=")) {
                    res.add(tok[0]);
                    res.add("=");
                    res.add(tok[1]);
                } else if (token.contains("!=")) {
                    res.add(tok[0]);
                    res.add("!=");
                    res.add(tok[1]);
                } else if (token.contains("<=")) {
                    res.add(tok[0]);
                    res.add("<=");
                    res.add(tok[1]);
                } else if (token.contains(">=")) {
                    res.add(tok[0]);
                    res.add(">=");
                    res.add(tok[1]);
                } else if (token.contains(">")) {
                    res.add(tok[0]);
                    res.add(">");
                    res.add(tok[1]);
                } else if (token.contains("<")) {
                    res.add(tok[0]);
                    res.add("<");
                    res.add(tok[1]);
                } else {
                    res.add(tok[0]);
                }
            } else {
                res.add(token);
            }
        }
        return res;
    }

    public static String[] insertParser(String request) {
        String sequence = request.toUpperCase();

        int intoIndex = sequence.indexOf("VALUES");
        String values = request.substring(intoIndex + 6);

        String elements = values.replaceAll("\\s", "");

        return elements.split(",");
    }

    public static String[] updateParser(String request) {
        String[] elements = new String[2];
        String temp = request.toUpperCase();

        int setIndex = temp.indexOf("VALUES");
        int whereIndex = temp.indexOf("WHERE");

        String set;
        String setClause;
        String whereClause;


        if (whereIndex == -1) {
            set = request.substring(setIndex + 7);

            setClause = set.replaceAll(SET_CLAUSE_PATTERN, "");
            whereClause = "";
        } else {
            set = request.substring(setIndex + 7, whereIndex - 1);

            setClause = set.replaceAll(SET_CLAUSE_PATTERN, "");
            whereClause = request.substring(whereIndex + 6);
        }

        elements[0] = setClause;
        elements[1] = whereClause;

        return elements;
    }
}
