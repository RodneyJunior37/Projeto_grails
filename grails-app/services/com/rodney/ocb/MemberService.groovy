package com.rodney.ocb

import grails.web.servlet.mvc.GrailsParameterMap

class MemberService {

    // Método para salvar um novo membro
    def save(GrailsParameterMap params) {
        // Cria um novo objeto Member com base nos parâmetros recebidos
        Member member = new Member(params)

        // Chama um método de utilitário (AppUtil) para salvar e obter a resposta
        def response = AppUtil.saveResponse(false, member)

        // Valida o objeto Member
        if (member.validate()) {
            // Salva o membro no banco de dados com a opção de "flush" para gravar imediatamente
            member.save(flush: true)
            // Verifica se não houve erros ao salvar
            if (!member.hasErrors()){
                response.isSuccess = true
            }
        }
        // Retorna a resposta após tentar salvar o membro
        return response
    }

    // Método para atualizar um membro existente
    def update(Member member, GrailsParameterMap params) {
        // Define as propriedades do membro com base nos parâmetros recebidos
        member.properties = params

        // Chama um método de utilitário para salvar e obter a resposta
        def response = AppUtil.saveResponse(false, member)

        // Valida o Member
        if (member.validate()) {
            // Salva o membro no banco de dados com a opção de "flush" para gravar imediatamente
            member.save(flush: true)
            // Verifica se não houve erros ao salvar
            if (!member.hasErrors()){
                response.isSuccess = true
            }
        }
        // Retorna a resposta após tentar atualizar o membro
        return response
    }

    // Método para obter um membro pelo ID
    def getById(Serializable id) {
        return Member.get(id)
    }

    // Método para listar membros com base nos parâmetros recebidos
    def list(GrailsParameterMap params) {
        // Define um limite máximo de itens por página, se não estiver definido nos parâmetros
        params.max = params.max ?: GlobalConfig.itemsPerPage()

        // Cria uma consulta para obter a lista de membros com base nos parâmetros
        List<Member> memberList = Member.createCriteria().list(params) {
            // Condição para pesquisar por um nome de coluna e valor específicos
            if (params?.colName && params?.colValue) {
                like(params.colName, "%" + params.colValue + "%")
            }
            // Ordena por ID decrescente se não houver ordenação especificada
            if (!params.sort) {
                order("id", "desc")
            }
        }
        // Retorna a lista de membros e o total de membros encontrados
        return [list: memberList, count: memberList.totalCount]
    }

    // Método para excluir um membro
    def delete(Member member) {
        try {
            // Tenta excluir o membro do banco de dados
            member.delete(flush: true)
        } catch (Exception e) {
            // Se ocorrer uma exceção ao excluir, imprime a mensagem de erro
            println(e.getMessage())
            // Retorna false se houver falha na exclusão
            return false
        }
        // Retorna true se a exclusão der certo
        return true
    }
}
