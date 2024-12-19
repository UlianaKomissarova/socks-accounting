package dev.uliana.socks_accounting.exception;

public class ErrorMessage {
    public static final String COLOR_IS_BLANK = "Укажите цвет носков.";

    public static final String INVALID_HEX = "Невалидный цвет.";

    public static final String COTTON_IS_NULL = "Укажите процент хлопка.";

    public static final String COTTON_LESS_THAN_ZERO = "Процент хлопка не может быть меньше нуля.";

    public static final String COTTON_MORE_THAN_HUNDRED = "Процент хлопка не может быть больше ста.";

    public static final String COUNT_IS_NULL = "Укажите количество носков.";

    public static final String COUNT_IS_NEGATIVE = "Количество носков не может быть отрицательным.";
}