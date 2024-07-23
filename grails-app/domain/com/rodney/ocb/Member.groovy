package com.rodney.ocb

class Member {

    Integer id
    String firstName
    String lastName
    String email
    String password
    String memberType = GlobalConfig.USER_TYPE.REGULAR_MEMBER
    String identityHash
    Long identityHashLastUpdate
    Boolean isActive = true

    Date dateCreated
    Date lastUpdated

    static constraints = {
        email(email: true, nullable: false, unique: true, blank: false)
        password(nullable: false)
        lastName(nullable: true)
        identityHash(nullable: true)
        identityHashLastUpdate(nullable: true)
    }

    // Método para criptografar a senha antes de inserir
    def beforeInsert() {
        password = password.encodeAsMD5()
    }

    // Método para criptografar a senha antes de atualizar
    def beforeUpdate() {
        password = password.encodeAsMD5()
    }

    static mapping = {
        version (false)

    }
}
