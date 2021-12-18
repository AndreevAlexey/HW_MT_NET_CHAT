package Constants;

public enum MsgType {
    USERNAME_EXISTS,
    CHAT_ENTER,
    CHAT_EXIT,
    USERNAME_ENTER,
    SERVER_RUN_ERROR,
    CLIENT_RUN_ERROR;

    @Override
    public String toString() {
        String result = null;
        switch(this) {
            case USERNAME_EXISTS :
                result = "Пользователь с таким ником уже присутствует в чате! Укажите другой.";
                break;
            case CHAT_ENTER :
                result = "зашел в чат";
                break;
            case CHAT_EXIT :
                result = "покинул чат";
                break;
            case USERNAME_ENTER :
                result = "Введите свой ник:";
                break;
            case SERVER_RUN_ERROR :
                result = "Ошибка запуска сервера: не указаны параметры сервера(порт, лог-файл)!";
                break;
            case CLIENT_RUN_ERROR :
                result = "Ошибка запуска клиента: не указаны параметры(хост, порт, лог-файл)!";
                break;


        }
        return result;
    }
}
