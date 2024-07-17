package net.andreabattista.InOutFlow.model;

public enum AccountType {

    /**
     * Can manage all the data of all the companies that use the web app.
     * */
    ADMINISTRATOR,

    /**
     * Can manage all of his company's data.
     * */
    SUPPORT,

    /**
     * Can only view his data.
     * */
    USER,
}
