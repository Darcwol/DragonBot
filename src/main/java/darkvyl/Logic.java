package darkvyl;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;

import java.util.Date;
import java.util.Random;

class Logic {

    static SendMessage TextResponse(Message message) {
        String response = "";
        SendMessage sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setReplyToMessageId(message.getMessageId());
        if (message.hasText()) {
            if (message.getText().equals("Дракоша, а кто у нас в отряде?")) {
                for (User value : DragonBot.users) {
                    if(value.getAttack() == 0)continue;
                    response = response.concat(value + "\n");
                }
            }
            if (message.getText().contains("/add_dragon")) {
                if ((message.getFrom().getUserName().equals("CaMoBaPiK") || message.getFrom().getUserName().equals("Darkvyl") || message.getFrom().getUserName().equals("Vasilisssa"))) {
                    try {
                        if (DBWork.Insert(new User()
                                .setId(message.getReplyToMessage().getFrom().getId().toString())
                                .setName(message.getReplyToMessage().getFrom().getFirstName())
                                .setAttack(0)
                                .setLevel("")
                                .setNick("")
                                .setProfession(""))) {
                            response = "Есть, новый боец записан!";
                        } else {
                            response = "Ты чего, это уже и так наш боец!";
                        }
                    } catch (Exception e) {
                        response = "Не понимаю, что?";
                    }
                }
                else{
                    response = " 502 Error! ";
                }
            }
            if (message.getText().contains("/del_dragon")) {
                if ((message.getFrom().getUserName().equals("CaMoBaPiK") ||
                        message.getFrom().getUserName().equals("Darkvyl") ||
                        message.getFrom().getUserName().equals("Vasilisssa"))) {
                    try {
                        if (DBWork.Delete(message.getReplyToMessage().getFrom().getId().toString())) {
                            response = "Пошёл вон от нас!";
                        } else {
                            response = "Эт хто?";
                        }
                    } catch (Exception e) {
                        response = "Не понимаю, что?";
                    }
                }
                else{
                    response = " 502 Error! ";
                }
            }
            if(message.getText().contains("/chlen@OysterDragonBot"))
            {
                response = "/chlenFromDragon!";
            }
            try {
                if (message.getForwardFrom().getUserName().equals("ChatWarsBot")
                        && message.getText().contains(" Синего замка")
                        && message.getChat().isUserChat()) {
                    User user = new User(
                            message.getFrom().getId().toString(),
                            message.getFrom().getFirstName(),
                            message.getText().substring(message.getText().indexOf("\uD83C\uDDEA\uD83C\uDDFA") + 4,
                                    message.getText().indexOf(",")),
                            message.getText().substring(message.getText().indexOf(",") + 1,
                                    message.getText().indexOf(" Синего замка")),
                            message.getText().substring(message.getText().indexOf(": ") + 2,
                                    message.getText().indexOf("\n⚔Атака:")),
                            Integer.valueOf(message.getText().substring(message.getText().indexOf("⚔Атака:") + 8,
                                    message.getText().indexOf(" 🛡Защита:"))));
                    if(DBWork.Update(user)){
                        response = "Молодец, славный воин Дракона!";
                    }
                    else{
                        response = "Ну и что ты хочешь, шпион???";
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        sendMessage.setText(response);
        return sendMessage;
    }

    static SendSticker checkSticker(Message msg) {
        SendSticker response = new SendSticker();
        response.setSticker("");
        response.setChatId(msg.getChatId());
        response.setReplyToMessageId(msg.getMessageId());
        try {
            if (msg.getSticker().getFileId().equals("CAADAgADiQAD6st5AuZbw2Z4SeORAg")) {
                response.setSticker("CAADAgADVAEAAs-71A5LL_taqiW6GQI");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            if (msg.getText().equals("Спокойной ночи")) {
                Random r = new Random();
                r.setSeed(new Date().getTime());
                int a = r.nextInt(4);
                switch (a) {
                    case 0:
                        response.setSticker("CAADAgADYAEAAs-71A54r4SYJQ5_cQI");
                        break;
                    case 1:
                        response.setSticker("CAADBAADdQEAAt59GQxcW9aBCp0I-wI");
                        break;
                    case 2:
                        response.setSticker("CAADAgADpAEAAmX_kgpN2xyC_MVfdQI");
                        break;
                    case 3:
                        response.setSticker("CAADAgADGAAD_6QEDjI5GBchsq0KAg");
                        break;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return response;
    }

    static EditMessageText CallbackQuery(CallbackQuery cq) {
        int message_id = cq.getMessage().getMessageId();
        long chat_id = cq.getMessage().getChatId();
        String user_id = cq.getFrom().getId().toString();
        String username = "";
        int attack = 0;
        for (User user : DragonBot.users) {
            if (user.getId().equals(user_id)){
                username = user.getNick().isEmpty()?cq.getFrom().getUserName():user.getNick()
                        + " ⚔" + user.getAttack();
                attack = user.getAttack();
            }
        }
        if (username.isEmpty()) {
            username = cq.getFrom().getUserName();
        }
        if (cq.getMessage().getText().contains(username)) {
            DragonBot.sumAttack-=attack;
            return new EditMessageText()
                    .setChatId(chat_id)
                    .setMessageId(message_id)
                    .setReplyMarkup(DragonBot.markupInline)
                    .setText(DragonBot.editedLine =
                            DragonBot.editedLine.replace("\n->" + username, "")
                    .replace(DragonBot.editedLine.substring(
                            DragonBot.editedLine.indexOf("\nСуммарная атака: ")),
                            "\nСуммарная атака: " + DragonBot.sumAttack)
                    );
        } else {
            DragonBot.sumAttack+=attack;
            return new EditMessageText()
                    .setChatId(chat_id)
                    .setMessageId(message_id)
                    .setReplyMarkup(DragonBot.markupInline)
                    .setText(DragonBot.editedLine = DragonBot.editedLine.replace(
                            DragonBot.editedLine.substring(DragonBot.editedLine.indexOf("\nСуммарная атака: ")),
                            "\n->" + username+"\nСуммарная атака: " + DragonBot.sumAttack));
        }

    }
}
