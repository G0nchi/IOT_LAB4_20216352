package com.example.iot_lab4_20216352.model;

import com.google.gson.annotations.SerializedName;

// LLM: clase generada con asistencia de IA para mapear la respuesta JSON de TheMealDB
public class Category {
    @SerializedName("idCategory")
    private String idCategory;

    @SerializedName("strCategory")
    private String strCategory;

    @SerializedName("strCategoryThumb")
    private String strCategoryThumb;

    @SerializedName("strCategoryDescription")
    private String strCategoryDescription;

    public String getIdCategory() { return idCategory; }
    public String getStrCategory() { return strCategory; }
    public String getStrCategoryThumb() { return strCategoryThumb; }
    public String getStrCategoryDescription() { return strCategoryDescription; }
}
