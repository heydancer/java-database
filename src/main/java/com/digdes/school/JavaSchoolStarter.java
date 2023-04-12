package com.digdes.school;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.digdes.school.Crud.DELETE;
import static com.digdes.school.Crud.INSERT;
import static com.digdes.school.Crud.SELECT;
import static com.digdes.school.Crud.UPDATE;

public class JavaSchoolStarter {
    private final List<Map<String, Object>> data = new ArrayList<>();

    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        String typeOfTransaction = request.toUpperCase();

        if (typeOfTransaction.startsWith(INSERT.name())) {
            insert(request);

            return data;

        } else if (typeOfTransaction.startsWith(UPDATE.name())) {
            String[] values = Formatter.updateParser(request);
            update(values[0], values[1]);

            return data;

        } else if (typeOfTransaction.startsWith(SELECT.name())) {
            int whereIndex = typeOfTransaction.indexOf("WHERE");
            String whereClause = request.substring(whereIndex + 6);

            if (whereIndex == -1) {
                return data;
            } else {
                return find(whereClause);
            }

        } else if (typeOfTransaction.startsWith(DELETE.name())) {
            int whereIndex = typeOfTransaction.indexOf("WHERE");
            String whereClause = request.substring(whereIndex + 6);

            if (whereIndex == -1) {
                data.clear();
            } else {
                delete(whereClause);
            }

            return data;

        } else {
            throw new ValidationException(
                    String.format("Unsupported operation: %s", request));
        }
    }

    private void delete(String clause) {
        for (Map<String, Object> row : data) {
            if (evaluateClause(clause, row)) {
                row.clear();
            }
        }
    }

    private void insert(String request) {
        Map<String, Object> newRow = createEmptyRow();

        String[] tokens = Formatter.insertParser(request);

        for (String token : tokens) {
            String[] keyValue = token.split("=");

            String key = keyValue[0].substring(1, keyValue[0].length() - 1);

            Object value;
            if (keyValue[1].startsWith("'") && keyValue[1].endsWith("'")) {
                value = keyValue[1].substring(1, keyValue[1].length() - 1);
            } else {
                value = keyValue[1];
            }

            if (!newRow.containsKey(key)) {
                throw new ValidationException(
                        String.format("Incorrect row: %s", key));
            }

            if (key.equals("id")) {
                try {
                    newRow.put(key, Long.parseLong(String.valueOf(value)));
                } catch (NumberFormatException exception) {
                    throw new ValidationException(
                            String.format("Id must be long. Id: %s", value));
                }
            }

            if (key.equals("lastName")) {
                newRow.put(key, String.valueOf(value));
            }

            if (key.equals("age")) {
                try {
                    newRow.put(key, Long.parseLong(String.valueOf(value)));
                } catch (NumberFormatException exception) {
                    throw new ValidationException(
                            String.format("Age must be long. Age: %s", value));
                }
            }

            if (key.equals("cost")) {
                try {
                    newRow.put(key, Double.parseDouble(String.valueOf(value)));
                } catch (NumberFormatException exception) {
                    throw new ValidationException(
                            String.format("Cost must be double. Cost: %s", value));
                }
            }

            if (key.equals("active")) {
                if (value.equals("true") || value.equals("false")) {
                    newRow.put(key, Boolean.parseBoolean(String.valueOf(value)));
                } else {
                    throw new ValidationException(
                            String.format("Active must be true or false. Active: %s", value));
                }
            }
        }
        data.add(newRow);
    }

    private void update(String setClause, String whereClause) {
        String[] setValues = setClause.split(",");
        for (Map<String, Object> row : data) {
            if (whereClause.isEmpty()) {
                for (Map.Entry<String, Object> ignored : row.entrySet()) {
                    for (String setValue : setValues) {
                        String[] splitSetValue = setValue.trim().split("=");
                        String columnName = splitSetValue[0].trim();
                        Object newValue = splitSetValue[1].trim();

                        if (columnName.equals("id")) {
                            try {
                                row.put(columnName, Long.parseLong(String.valueOf(newValue)));
                            } catch (NumberFormatException exception) {
                                throw new ValidationException(
                                        String.format("Id must be long. Id: %s", newValue));
                            }
                        }

                        if (columnName.equals("lastName")) {
                            row.put(columnName, String.valueOf(newValue));
                        }

                        if (columnName.equals("age")) {
                            try {
                                row.put(columnName, Long.parseLong(String.valueOf(newValue)));
                            } catch (NumberFormatException exception) {
                                throw new ValidationException(
                                        String.format("Age must be long. Age: %s", newValue));
                            }
                        }

                        if (columnName.equals("cost")) {
                            try {
                                row.put(columnName, Double.parseDouble(String.valueOf(newValue)));
                            } catch (NumberFormatException exception) {
                                throw new ValidationException(
                                        String.format("Cost must be double. Cost: %s", newValue));
                            }
                        }

                        if (columnName.equals("active")) {
                            if (newValue.equals("true") || newValue.equals("false")) {
                                row.put(columnName, Boolean.parseBoolean(String.valueOf(newValue)));
                            } else {
                                throw new ValidationException(
                                        String.format("Active must be true or false. Active: %s", newValue));
                            }
                        }
                        //row.put(columnName, newValue);
                    }
                }
            } else {
                if (evaluateClause(whereClause, row)) {
                    for (Map.Entry<String, Object> ignored : row.entrySet()) {
                        for (String setValue : setValues) {
                            String[] splitSetValue = setValue.trim().split("=");
                            String columnName = splitSetValue[0].trim();
                            String newValue = splitSetValue[1].trim();
                            if (columnName.equals("id")) {
                                try {
                                    row.put(columnName, Long.parseLong(newValue));
                                } catch (NumberFormatException exception) {
                                    throw new ValidationException(
                                            String.format("Id must be long. Id: %s", newValue));
                                }
                            }

                            if (columnName.equals("lastName")) {
                                row.put(columnName, newValue);
                            }

                            if (columnName.equals("age")) {
                                try {
                                    row.put(columnName, Long.parseLong(newValue));
                                } catch (NumberFormatException exception) {
                                    throw new ValidationException(
                                            String.format("Age must be long. Age: %s", newValue));
                                }
                            }

                            if (columnName.equals("cost")) {
                                try {
                                    row.put(columnName, Double.parseDouble(newValue));
                                } catch (NumberFormatException exception) {
                                    throw new ValidationException(
                                            String.format("Cost must be double. Cost: %s", newValue));
                                }
                            }

                            if (columnName.equals("active")) {
                                if (newValue.equals("true") || newValue.equals("false")) {
                                    row.put(columnName, Boolean.parseBoolean(newValue));
                                } else {
                                    throw new ValidationException(
                                            String.format("Active must be true or false. Active: %s", newValue));
                                }
                            }

                            //row.put(columnName, newValue);
                        }
                    }
                }
            }
        }
    }

    private List<Map<String, Object>> find(String clause) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> row : data) {
            if (evaluateClause(clause, row)) {
                result.add(row);
            }
        }

        return result;
    }

    private Map<String, Object> createEmptyRow() {
        Long defaultId = null;
        String defaultLastName = "";
        Long defaultAge = null;
        Double defaultCost = 0.0;
        Boolean defaultActive = false;

        Map<String, Object> newRow = new LinkedHashMap<>();
        newRow.put("id", defaultId);
        newRow.put("lastName", defaultLastName);
        newRow.put("age", defaultAge);
        newRow.put("cost", defaultCost);
        newRow.put("active", defaultActive);

        return newRow;
    }

    private boolean evaluateClause(String clause, Map<String, Object> row) {
        List<String> tokens = Formatter.conditionsParser(clause);

        List<String> leftOperand = new ArrayList<>();
        List<String> operators = new ArrayList<>();
        List<String> rightOperand = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i = i + 3) {
            if (tokens.get(i).equalsIgnoreCase("AND") || tokens.get(i).equalsIgnoreCase("OR")) {
                leftOperand.add(tokens.get(i).toUpperCase());
                operators.add(tokens.get(i).toUpperCase());
                rightOperand.add(tokens.get(i).toUpperCase());
                i = i - 2;
            } else if (tokens.get(i).equalsIgnoreCase("LIKE") || tokens.get(i).equalsIgnoreCase("ILIKE")) {
                leftOperand.add(tokens.get(i - 1));
                operators.add(tokens.get(i));
                rightOperand.add(tokens.get(i + 1));
                i = i + 2;
            } else {
                leftOperand.add(String.valueOf(row.get(tokens.get(i))));
                operators.add(tokens.get(i + 1));
                rightOperand.add(tokens.get(i + 2));
            }
        }

        List<Boolean> booleans = new ArrayList<>();
        List<Boolean> allTrue = new ArrayList<>();

        for (int i = 0; i < operators.size(); i++) {
            allTrue.add(true);
        }

        for (int i = 0; i < operators.size(); i++) {
            String left;
            String right;
            String operator = operators.get(i).toUpperCase();
            switch (operator) {
                case ">":
                    if (Double.parseDouble(String.valueOf(
                            leftOperand.get(i))) > Double.parseDouble(String.valueOf(rightOperand.get(i)))) {
                        booleans.add(true);
                    } else {
                        booleans.add(false);
                    }
                    break;
                case ">=":
                    if (Double.parseDouble(String.valueOf(
                            leftOperand.get(i))) >= Double.parseDouble(String.valueOf(rightOperand.get(i)))) {
                        booleans.add(true);
                    } else {
                        booleans.add(false);
                    }
                    break;
                case "<":
                    if (Double.parseDouble(String.valueOf(
                            leftOperand.get(i))) < Double.parseDouble(String.valueOf(rightOperand.get(i)))) {
                        booleans.add(true);
                    } else {
                        booleans.add(false);
                    }
                    break;
                case "<=":
                    if (Double.parseDouble(String.valueOf(
                            leftOperand.get(i))) <= Double.parseDouble(String.valueOf(rightOperand.get(i)))) {
                        booleans.add(true);
                    } else {
                        booleans.add(false);
                    }
                    break;
                case "=":
                    if (leftOperand.get(i).equals(rightOperand.get(i))) {
                        booleans.add(true);
                    } else {
                        booleans.add(false);
                    }
                    break;
                case "!=":
                    if (!leftOperand.get(i).equals(rightOperand.get(i))) {
                        booleans.add(true);
                    } else {
                        booleans.add(false);
                    }
                    break;
                case "LIKE":
                    left = String.valueOf(leftOperand.get(i));
                    right = String.valueOf(rightOperand.get(i));

                    if (right.startsWith("%") && right.endsWith("%")) {
                        String temp = right.replaceAll("%", "");

                        if (left.contains(temp)) {
                            booleans.add(true);
                        } else {
                            booleans.add(false);
                        }
                    } else if (right.endsWith("%")) {
                        String temp = right.replace("%", "");

                        if (left.startsWith(temp)) {
                            booleans.add(true);
                        } else {
                            booleans.add(false);
                        }
                    } else if (right.startsWith("%")) {
                        String temp = right.replace("%", "");

                        if (left.endsWith(temp)) {
                            booleans.add(true);
                        } else {
                            booleans.add(false);
                        }
                    }
                    break;
                case "ILIKE":
                    left = String.valueOf(leftOperand.get(i)).toLowerCase();
                    right = String.valueOf(rightOperand.get(i)).toLowerCase();

                    if (right.startsWith("%") && right.endsWith("%")) {
                        String temp = right.replaceAll("%", "");

                        if (left.contains(temp)) {
                            booleans.add(true);
                        } else {
                            booleans.add(false);
                        }
                    } else if (right.endsWith("%")) {
                        String temp = right.replace("%", "");

                        if (left.startsWith(temp)) {
                            booleans.add(true);
                        } else {
                            booleans.add(false);
                        }
                    } else if (right.startsWith("%")) {
                        String temp = right.replace("%", "");

                        if (left.endsWith(temp)) {
                            booleans.add(true);
                        } else {
                            booleans.add(false);
                        }
                    }
                    break;
                case "AND":
                case "OR":
                    if (booleans.contains(true)) {
                        booleans.add(true);
                    } else {
                        booleans.add(false);
                    }
                    break;
                default:
                    throw new ValidationException(
                            String.format("Invalid operator: %s", operator));
            }
        }

        if (operators.contains("AND") && operators.contains("OR")) {
            int andIndex = operators.indexOf("AND");
            int orIndex = operators.indexOf("OR");
            List<Boolean> allBoolBeforeOR = booleans.subList(0, orIndex);
            List<Boolean> allBoolAfterOR = booleans.subList(orIndex + 1, booleans.size());

            if (orIndex > andIndex) {
                boolean allBoolBeforeOREquals = allBoolBeforeOR.stream().allMatch(b -> b);
                return allBoolBeforeOREquals || allBoolAfterOR.contains(true);
            } else {
                boolean allBoolAfterOREquals = allBoolAfterOR.stream().allMatch(b -> b);
                return allBoolBeforeOR.contains(true) || allBoolAfterOREquals;
            }

        } else if (operators.contains("AND")) {
            return booleans.equals(allTrue);
        } else if (operators.contains("OR")) {
            return booleans.contains(true);
        } else {
            return new HashSet<>(booleans).containsAll(allTrue);
        }
    }
}










