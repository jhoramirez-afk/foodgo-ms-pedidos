package cl.duoc.jv0101.foodgo.pedidos.exception;

public class BusinessRuleException extends RuntimeException {
    private final String field;

    public BusinessRuleException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() { return field; }
}
