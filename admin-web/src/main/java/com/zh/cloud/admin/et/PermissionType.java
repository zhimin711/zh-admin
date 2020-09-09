package com.zh.cloud.admin.et;


/**
 * decs:
 *
 * @author 01370603
 * @date 2020/9/8
 */
public enum PermissionType {
    CATALOG("C", "目录"), MENU("M", "菜单"), BUTTON("B", "按钮"), ALL("", "");
    private final String code;
    private final String name;

    PermissionType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static boolean isCatalog(String code) {
        return CATALOG.code.equals(code);
    }

    public static boolean isMenu(String code) {
        return MENU.code.equals(code);
    }

    public static boolean isButton(String code) {
        return BUTTON.code.equals(code);
    }

    public static Object[] getCatalogMenuCodes() {
        return new Object[]{CATALOG.code, MENU.code};
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PermissionType from(String code) {
        if (code == null || code.trim().length() == 0) {
            return ALL;
        }
        switch (code.trim().toUpperCase()) {
            case "C":
                return CATALOG;
            case "M":
                return MENU;
            case "B":
                return BUTTON;
        }
        return ALL;
    }
}
