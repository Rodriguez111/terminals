package terminals.models;

public enum  Pages {
    MAIN_JSP("/WEB-INF/terminals/viewer/main.jsp"),
    MAIN_USERS_JSP ("/WEB-INF/terminals/viewer/usersview/main_users.jsp"),
    ADD_USER_JSP("/WEB-INF/terminals/viewer/usersview/add_user.jsp"),
    UPDATE_USER_JSP("/WEB-INF/terminals/viewer/usersview/update_user.jsp"),


    MAIN_TERMINALS_JSP("/WEB-INF/terminals/viewer/terminalsview/main_terminals.jsp"),
    ADD_TERMINAL_JSP("/WEB-INF/terminals/viewer/terminalsview/add_terminal.jsp"),
    UPDATE_TERMINAL_JSP("/WEB-INF/terminals/viewer/terminalsview/update_terminal.jsp"),
    GENERATE_BARCODE_JSP("/WEB-INF/terminals/viewer/terminalsview/generate_barcode.jsp"),
    DISPLAY_BARCODE_JSP("/WEB-INF/terminals/viewer/terminalsview/display_barcodes.jsp"),

    MAIN_DEPS_JSP ("/WEB-INF/terminals/viewer/departmentsview/main_depart.jsp"),
    ADD_DEPART_JSP("/WEB-INF/terminals/viewer/departmentsview/add_depart.jsp"),

    MAIN_REGS_JSP ("/WEB-INF/terminals/viewer/registrations/regs.jsp");





    public final String page;

    Pages(String page) {
        this.page = page;
    }
}
