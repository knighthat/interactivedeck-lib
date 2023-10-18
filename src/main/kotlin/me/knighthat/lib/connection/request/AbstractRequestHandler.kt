package me.knighthat.lib.connection.request

import me.knighthat.lib.connection.Connection

abstract class AbstractRequestHandler {

    protected abstract fun handleAddRequest(request: AddRequest)

    protected abstract fun handleRemoveRequest(request: RemoveRequest)

    protected abstract fun handleUpdateRequest(request: UpdateRequest)

    protected abstract fun handlePairRequest(request: Request)

    protected abstract fun handleActionRequest(request: ActionRequest)

    fun process(request: Request) {
        if (request is RequiredConnection && !Connection.isConnected())
            return

        when (request.type) {

            Request.RequestType.ADD    -> handleAddRequest(request as AddRequest)
            Request.RequestType.REMOVE -> handleRemoveRequest(request as RemoveRequest)
            Request.RequestType.UPDATE -> handleUpdateRequest(request as UpdateRequest)
            Request.RequestType.PAIR   -> handlePairRequest(request)
            Request.RequestType.ACTION -> handleActionRequest(request as ActionRequest)
        }
    }
}