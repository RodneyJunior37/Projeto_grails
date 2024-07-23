OCB.messageBox = (function () {
    // HTML do template da mensagem de alerta
    var messageHtml = '<div class="alert"><span class="message"></span></div>';

    // Variável para armazenar o ID do timeout para ocultar a mensagem
    var timeOut = null;

    // Função interna para ocultar a mensagem de alerta
    function hideMessage() {
        $(".alert").hide();  // Esconde qualquer elemento com a classe ".alert"
        timeOut = null;      // Limpa o timeout
    }

    // Função interna para verificar e limpar o timeout atual
    function checkTimeOut() {
        if (timeOut !== null) {
            clearTimeout(timeOut);  // Cancela o timeout atual se existir
        }
    }

    // Retorna um objeto com métodos públicos
    return {
        // Método público para mostrar a mensagem de alerta
        showMessage: function(success, message) {
            // Verifica se não existe nenhum elemento com a classe ".alert" na página
            if (!jQuery(".alert").length) {
                jQuery(document.body).append(messageHtml);  // Adiciona o HTML da mensagem ao corpo do documento
            }

            // Define o tipo de mensagem com base no parâmetro success
            var messageType = "alert-success";
            if (success !== true) {
                messageType = "alert-danger";
            }

            // Limpa o timeout atual, se houver
            checkTimeOut();

            // Seleciona o elemento da mensagem
            var messageElement = $(document.body).find(".alert");

            // Define o texto da mensagem no elemento
            messageElement.find(".message").text(message);

            // Remove as classes existentes e adiciona a classe do tipo de mensagem
            messageElement.removeClass("alert-success").removeClass("alert-danger").addClass(messageType);

            // Exibe o elemento da mensagem
            messageElement.show();

            // Define um novo timeout para ocultar a mensagem após 5 segundos (5000 milissegundos)
            timeOut = setTimeout(function() {
                hideMessage();
            }, 5000);
        }
    };
}());
