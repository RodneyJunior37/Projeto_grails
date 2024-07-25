package com.rodney.ocb

class Contact {

    Integer id
    String name
    String image
    Member member

    Date dateCreated
    Date lastUpdated

    Set<ContactDetails> contactDetails
    Set<ContactGroup> contactGroup


    static hasMany = [contactDetails: ContactDetails, contactGroup: ContactGroup]

    static constraints = {
        image(nullable: true, blank: true)
    }

    static mapping = {
        version(false)
        contactDetails(cascade: 'all-delete-orphan')//garante que ao excluir o contato tamêm sera excluido os todos details
    }

}
