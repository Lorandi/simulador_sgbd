package enums;

public enum OptionsMenu {

    START_TRANSACTION("1", "Inicia transação"),
    CHOOSE_TRANSACTION("2", "Escolhe transação"),
    END_TRANSACTION("3", "Finaliza transação"),
    LOG_BUFFER("4", "Log no buffer"),
    LOG_DISC("5", "Log no disco"),
    DATABASE_BUFFER("6", "Dados no buffer"),
    DATABASE_DISC("7", "Dados no disco"),
    CHECK_POINT("8", "Checkpoint"),
    FAIL("9", "Falha"),
    EXIT("10", "Sair"),
    ERROR("", "Erro");

    final String value;
    final String description;

    OptionsMenu(String valueOption, String descriptionOption) {
        this.value = valueOption;
        this.description = descriptionOption;
    }

    public String getValue() {
        return value;
    }

    public String getDescription(){ return description;}


    public static OptionsMenu getByCode(String code) {
        for (OptionsMenu e : OptionsMenu.values()) {
            if (e.getValue().equals(code)) {
                return e;
            }
        }
        return ERROR;
    }
}