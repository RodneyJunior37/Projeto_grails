package novo

import com.rodney.ocb.AppInitializationService

class BootStrap {

    def init = { servletContext ->
        AppInitializationService.initialize()
    }
    def destroy = {
    }
}
