;(function(){
    const modal = new bootstrap.Modal(document.getElementById('modalpax2'))

    htmx.on('htmx:afterSwap', (e) => {
        if (e.detail.target.id === "dialog_pax2")
            modal.show()
    })

    htmx.on('htmx:beforeSwap', (e) => {
        if (e.detail.target.id === "dialog_pax2" && !e.detail.xhr.response)
            modal.hide()
    })
})()