package org.motechproject.commcare.domain;

import java.util.HashMap;
import java.util.Map;

public class Case {
    private String case_id;
    private String date_modified;
    private String action;
    
    private Map<String,String> fieldValues;
    private String case_type;
    private String case_name;

    public String getUser_id() {
        return user_id;
    }

    private String user_id;

    public Case(){
        fieldValues = new HashMap<String, String>();
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getAction() {
        return action;
    }


    public void setAction(String action) {
        this.action = action;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public void setCase_name(String case_name) {
        this.case_name = case_name;
    }

    public void AddFieldvalue(String nodeName, String nodeValue) {
        fieldValues.put(nodeName,nodeValue);
    }

    public String getCase_id() {
        return case_id;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public String getCase_type() {
        return case_type;
    }

    public String getCase_name() {
        return case_name;
    }

    public Map<String, String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(Map<String, String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public void setUser_id(String userId) {
        this.user_id =   userId;
    }
}
