package com.rodney.ocb

import grails.web.servlet.mvc.GrailsParameterMap

// Importa as classes necessárias

class ContactGroupService {

    AuthenticationService authenticationService

    // Propriedade que injeta o serviço de autenticação

    def save(def params) {
        // Método para salvar um novo grupo de contatos
        ContactGroup contactGroup = new ContactGroup(params)
        // Cria uma nova instância de ContactGroup com os parâmetros fornecidos
        contactGroup.member = authenticationService.getMember()
        // Associa o membro atual autenticado ao grupo de contatos
        def response = AppUtil.saveResponse(false, contactGroup)
        // Utiliza um utilitário para salvar a resposta

        if (contactGroup.validate()) {
            // Verifica se o grupo de contatos é válido
            response.isSuccess = true
            // Define que a operação foi bem sucedida na resposta
            contactGroup.save()
            // Salva o grupo de contatos
        }
        return response
        // Retorna a resposta do salvamento
    }

    def update(ContactGroup contactGroup, GrailsParameterMap params) {
        // Método para atualizar um grupo de contatos existente
        contactGroup.properties = params
        // Atualiza as propriedades do grupo de contatos com os novos parâmetros
        def response = AppUtil.saveResponse(false, contactGroup)
        // Utiliza um utilitário para salvar a resposta

        if (contactGroup.validate()) {
            // Verifica se o grupo de contatos é válido
            response.isSuccess = true
            // Define que a operação foi bem sucedida na resposta
            contactGroup.save(flush:true)
            // Salva o grupo de contatos, forçando a gravação imediata no banco de dados
        }
        return response
        // Retorna a resposta do salvamento
    }

    def get(Serializable id) {
        // Método para obter um grupo de contatos pelo ID
        return ContactGroup.get(id)
        // Retorna o grupo de contatos encontrado pelo ID fornecido
    }

    def list(GrailsParameterMap params) {
        // Método para listar grupos de contatos com base nos parâmetros fornecidos
        params.max = params.max ?: GlobalConfig.itemsPerPage()
        // Define o número máximo de resultados por página, utilizando uma configuração global

        List<ContactGroup> contactGroupList = ContactGroup.createCriteria().list(params) {
            // Cria uma consulta usando critérios para o modelo ContactGroup, com os parâmetros fornecidos
            if (params?.colName && params?.colValue){
                like(params.colName, "%" + params.colValue + "%")
                // Adiciona uma condição de pesquisa parcial (like) se colName e colValue estiverem presentes nos parâmetros
            }
            if (!params.sort){
                order("id","desc")
                // Define a ordenação padrão por ID em ordem decrescente, se não houver ordenação especificada
            }
            eq("member", authenticationService.getMember())
            // Adiciona uma condição de igualdade para o membro atual autenticado
        }

        return [list: contactGroupList, count: contactGroupList.totalCount]
        // Retorna um mapa com a lista de grupos de contatos e o total de resultados encontrados
    }

    def getGroupList(){
        // Método para obter a lista de grupos de contatos do membro atual autenticado
        return ContactGroup.createCriteria().list {
            eq("member", authenticationService.getMember())
            // Cria uma consulta utilizando critérios para o modelo ContactGroup, filtrando pelo membro atual autenticado
        }
    }

    def cleanGroupContactById(Integer id){
        // Método para limpar contatos de um grupo de contatos específico pelo ID
        ContactGroup contactGroup = ContactGroup.get(id)
        // Obtém o grupo de contatos pelo ID fornecido
        contactGroup.contact.each { contact ->
            contact.removeFromContactGroup(contactGroup)
            // Para cada contato no grupo, remove o grupo de contatos associado a ele
        }
        contactGroup.save(flush:true)
        // Salva o grupo de contatos com a remoção dos relacionamentos, forçando a gravação imediata no banco de dados
    }

    def delete(ContactGroup contactGroup) {
        // Método para deletar um grupo de contatos
        try {
            cleanGroupContactById(contactGroup.id)
            // Chama o método para limpar os contatos do grupo antes de deletar
            contactGroup.delete(flush: true)
            // Deleta o grupo de contatos do banco de dados, forçando a gravação imediata
        } catch (Exception e) {
            println(e.getMessage())
            // Em caso de exceção, imprime a mensagem de erro
            return false
            // Retorna falso indicando que a operação de deleção falhou
        }
        return true
        // Retorna verdadeiro indicando que a operação de deleção foi bem sucedida
    }
}
