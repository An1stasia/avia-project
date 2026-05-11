;(function(){
    const modal = new bootstrap.Modal(document.getElementById('modallog'))

    htmx.on('htmx:afterSwap', (e) => {
        if (e.detail.target.id === "dialog3")
            modal.show()
    })

    htmx.on('htmx:beforeSwap', (e) => {
        if (e.detail.target.id === "dialog3" && !e.detail.xhr.response)
            modal.hide()
    })
})()