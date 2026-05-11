;(function(){
    const modal = new bootstrap.Modal(document.getElementById('modalpax'))

    htmx.on('htmx:afterSwap', (e) => {
        if (e.detail.target.id === "dialog_pax1")
            modal.show()
    })

    htmx.on('htmx:beforeSwap', (e) => {
        if (e.detail.target.id === "dialog_pax1" && !e.detail.xhr.response)
            modal.hide()
    })
})()