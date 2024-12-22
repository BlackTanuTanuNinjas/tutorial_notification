const SendNotification = {
    mounted(){
        this.el.addEventListener('click', () => {
            window.Android.sendNotification()
        })

    }
}

export default SendNotification;