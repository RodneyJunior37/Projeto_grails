package com.rodney.ocb

class ContactGroupController {

    ContactGroupService contactGroupService
    // Injeção do serviço ContactGroupService para acesso às funcionalidades do serviço

    def index() {
        // Ação para exibir a lista de grupos de contatos
        def response = contactGroupService.list(params)
        // Chama o serviço para obter a lista de grupos de contatos, passando os parâmetros da requisição
        [contactGroups: response.list, total: response.count]
        // Retorna um mapa com a lista de grupos de contatos e o total de grupos encontrados
    }

    def details(Integer id) {
        // Ação para exibir detalhes de um grupo de contatos específico pelo ID
        def response = contactGroupService.get(id)
        // Chama o serviço para obter o grupo de contatos pelo ID
        if (!response) {
            redirect(controller: "contactGroup", action: "index")
            // Redireciona para a página inicial de grupos de contatos se o grupo não for encontrado
        } else {
            [contactGroup: response]
            // Retorna os detalhes do grupo de contatos encontrados
        }
    }

    def create() {
        // Ação para exibir o formulário de criação de novo grupo de contatos
        [contactGroup: flash.redirectParams]
        // Retorna os parâmetros de redirecionamento flash para preencher o formulário, se existirem
    }

    def save() {
        // Ação para salvar um novo grupo de contatos
        def response = contactGroupService.save(params)
        // Chama o serviço para salvar o grupo de contatos com os parâmetros recebidos
        if (response.isSuccess) {
            flash.message = AppUtil.infoMessage(g.message(code: "saved"))
            // Define uma mensagem de sucesso para exibição ao usuário
            redirect(controller: "contactGroup", action: "index")
            // Redireciona para a lista de grupos de contatos após salvar com sucesso
        } else {
            flash.redirectParams = response.model
            // Define os parâmetros de redirecionamento flash para preencher o formulário novamente
            flash.message = AppUtil.infoMessage(g.message(code: "unable.to.save"), false)
            // Define uma mensagem de erro para exibição ao usuário
            redirect(controller: "contactGroup", action: "create")
            // Redireciona de volta para o formulário de criação de grupo de contatos
        }
    }

    def edit(Integer id) {
        // Ação para exibir o formulário de edição de um grupo de contatos existente
        if (flash.redirectParams) {
            [contactGroup: flash.redirectParams]
            // Retorna os parâmetros de redirecionamento flash para preencher o formulário, se existirem
        } else {
            def response = contactGroupService.get(id)
            // Chama o serviço para obter o grupo de contatos pelo ID
            if (!response) {
                flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
                // Define uma mensagem de erro se o grupo de contatos não for encontrado
                redirect(controller: "contactGroup", action: "index")
                // Redireciona para a lista de grupos de contatos
            } else {
                [contactGroup: response]
                // Retorna os detalhes do grupo de contatos encontrados para preencher o formulário de edição
            }
        }
    }

    def update() {
        // Ação para atualizar um grupo de contatos existente
        def response = contactGroupService.get(params.id)
        // Chama o serviço para obter o grupo de contatos pelo ID recebido nos parâmetros
        if (!response) {
            flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
            // Define uma mensagem de erro se o grupo de contatos não for encontrado
            redirect(controller: "contactGroup", action: "index")
            // Redireciona para a lista de grupos de contatos
        } else {
            response = contactGroupService.update(response, params)
            // Chama o serviço para atualizar o grupo de contatos com os novos parâmetros recebidos
            if (!response.isSuccess) {
                flash.redirectParams = response.model
                // Define os parâmetros de redirecionamento flash para preencher o formulário novamente
                flash.message = AppUtil.infoMessage(g.message(code: "unable.to.update"), false)
                // Define uma mensagem de erro para exibição ao usuário
                redirect(controller: "contactGroup", action: "edit")
                // Redireciona de volta para o formulário de edição de grupo de contatos
            } else {
                flash.message = AppUtil.infoMessage(g.message(code: "updated"))
                // Define uma mensagem de sucesso para exibição ao usuário
                redirect(controller: "contactGroup", action: "index")
                // Redireciona para a lista de grupos de contatos após atualizar com sucesso
            }
        }
    }

    def delete(Integer id) {
        // Ação para deletar um grupo de contatos existente pelo ID
        def response = contactGroupService.get(id)
        // Chama o serviço para obter o grupo de contatos pelo ID recebido
        if (!response) {
            flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
            // Define uma mensagem de erro se o grupo de contatos não for encontrado
            redirect(controller: "contactGroup", action: "index")
            // Redireciona para a lista de grupos de contatos
        } else {
            response = contactGroupService.delete(response)
            // Chama o serviço para deletar o grupo de contatos encontrado
            if (!response) {
                flash.message = AppUtil.infoMessage(g.message(code: "unable.to.delete"), false)
                // Define uma mensagem de erro se a deleção falhar
            } else {
                flash.message = AppUtil.infoMessage(g.message(code: "deleted"))
                // Define uma mensagem de sucesso se a deleção for bem-sucedida
            }
            redirect(controller: "contactGroup", action: "index")
            // Redireciona para a lista de grupos de contatos após a operação de deleção
        }
    }

}
