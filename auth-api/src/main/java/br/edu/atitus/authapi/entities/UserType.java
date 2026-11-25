package br.edu.atitus.authapi.entities;

public enum UserType {
    Admin(0),
    Common(1);

    private final int code;

    UserType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
