const SendNotification = {
    mounted(){
        userAgent = navigator.userAgent;

        if (/Android/.test(userAgent)) {
            this.el.addEventListener('click', () => {
                window.Android.sendNotification()
            })
        } else if (/Mac/.test(userAgent)) {
            this.el.addEventListener('click', () => {
                window.webkit.messageHandlers.sendNotification.postMessage("sendNotification");
            })
        }
    }
}

export default SendNotification;