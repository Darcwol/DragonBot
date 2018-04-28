package darkvyl;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.ForwardMessage;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.inlinequery.result.chached.InlineQueryResultCachedSticker;
import org.telegram.telegrambots.api.objects.inlinequery.result.chached.InlineQueryResultCachedVoice;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("SpellCheckingInspection")
class DragonBot extends TelegramLongPollingBot{
    static String editedLine;
    static ArrayList<User> users;
    static InlineKeyboardMarkup markupInline;
    static int sumAttack;

    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            users = DBWork.getAll();
            botsApi.registerBot(new DragonBot());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "Дракоша";
    }

    @Override
    public void onUpdateReceived(Update e) {
        if (e.hasMessage()) {
            onMessageRecive(e.getMessage());
        }
        if (e.hasCallbackQuery()) {
            onCallbackQueryRecive(e.getCallbackQuery());
        }
        if(e.hasInlineQuery()){
            onInlineQueryReceive(e.getInlineQuery());
        }
    }


    private void onMessageRecive(Message msg){
        boolean isDragon = false;
        for (User user : users) {

            if (user.getId().equals(msg.getFrom().getId().toString())) {
                isDragon = true;
                break;
            }
        }
        try {
            if (!(msg.getNewChatMembers().isEmpty())) {
                for (org.telegram.telegrambots.api.objects.User u : msg.getNewChatMembers()) {
                    execute(new SendMessage()
                            .setChatId(msg.getChatId())
                            .setText("Привет " + u.getUserName() + ", новый воин Дракона (или гость)! Мы рады всем!" +
                                    " Не стесняйся, представься. Расскажи немного о себе, откуда ты, что любишь. Нам интересно всё!"));
                }
            }
        } catch (Exception exp) {
            exp.getMessage();
        }
        if (isDragon) {
            SendMessage sendMessage = Logic.TextResponse(msg);
            SendSticker sendSticker = Logic.checkSticker(msg);
            try {
                execute(sendMessage);
            } catch (Exception e1) {
                e1.getMessage();
            }
            try {
                sendSticker(sendSticker);
            } catch (Exception e1) {
                e1.getMessage();
            }
            if (msg.hasText() && msg.getText().equals("Битва на носу, Дракоша!")) createPoll(msg.getChatId());
            if (msg.hasText() && msg.getText().equals("Report time!")) createReport(msg);
            if (msg.hasText() && msg.getText().equals("Драконий ор")) postToChanel(msg);
            if (msg.hasText() && msg.getText().contains("/delete")){
                try{
                    execute(new DeleteMessage().setMessageId(msg.getReplyToMessage().getMessageId()).setChatId(msg.getChatId().toString()));
                }
                catch (Exception e){
                    e.getMessage();
                }
            }
            try {
                if (msg.getFrom().getUserName().equals("Vasilisssa") ||
                        msg.getFrom().getUserName().equals("CaMoBaPiK") ||
                        msg.getFrom().getUserName().equals("Darkvyl")) {
                    if (msg.getText().contains("Рассылка:")) {
                        dispath(msg.getText().replace("Рассылка:", ""), false);
                    }
                    if (msg.getText().contains("Рассылка(тихая):")) {
                        dispath(msg.getText().replace("Рассылка(тихая):", ""), true);
                    }
                    if (msg.getText().contains("В главный чат:")) {
                        sendMsgToMainChat(msg.getText().replace("В главный чат:", ""));
                    }
                }
            } catch (Exception e1) {
                e1.getMessage();
            }
        }
        else {
            if (msg.getChat().isUserChat()) {
                try {
                    execute(new SendMessage().setChatId(msg.getChatId()).setText("Я ничего вам не скажу!"));
                } catch (Exception exp) {
                    exp.getMessage();
                }
            }
        }
    }
    private void onCallbackQueryRecive(CallbackQuery callbackQuery){
        if(callbackQuery.getData().equals("update_msg_text")) {
            EditMessageText messageText = Logic.CallbackQuery(callbackQuery);
            if (!(messageText.getText().isEmpty())) {
                try {
                    execute(messageText);
                } catch (Exception e1) {
                    e1.getMessage();
                }
            }
        }
    }
    private void onInlineQueryReceive(InlineQuery inlineQuery){
        AnswerInlineQuery answer = new AnswerInlineQuery().setInlineQueryId(inlineQuery.getId());
        ArrayList<InlineQueryResult> results = new ArrayList<>();
        if(inlineQuery.hasQuery()) {
            if (inlineQuery.getQuery().equals("Репорт!")) {
                InputTextMessageContent text = new InputTextMessageContent().setMessageText("/report");
                InlineQueryResultArticle report = new InlineQueryResultArticle().setInputMessageContent(text)
                        .setTitle("Репорт!").setId("report");
                results.add(report);
            }
            if (inlineQuery.getQuery().equals("бунт")) {
                InlineQueryResultCachedSticker bunt = new InlineQueryResultCachedSticker()
                        .setStickerFileId("CAADAgADFAAD_6QEDgJ-dgwuEUmaAg").setId("bunt");
                results.add(bunt);
            }
            if(inlineQuery.getQuery().equals("войсы")){
                results.add(new InlineQueryResultCachedVoice().setTitle("Петуч").setId("petuch").setVoiceFileId("AwADAgADRgEAAo6YwUpHysW277bazgI"));
                results.add(new InlineQueryResultCachedVoice().setTitle("Пізда").setId("pizda").setVoiceFileId("AwADAgADAQADlHphSgWRmAQVnBq1Ag"));
                results.add(new InlineQueryResultCachedVoice().setTitle("На костёр").setId("koster").setVoiceFileId("AwADAgADUQADV3aIStzJiGksMjIWAg"));
                results.add(new InlineQueryResultCachedVoice().setTitle("Отрицание").setId("no").setVoiceFileId("AwADAgADVAADV3aISsytq73bNOdEAg"));
                results.add(new InlineQueryResultCachedVoice().setTitle("Спокойной ночи").setId("gn").setVoiceFileId("AwADAgADVwADV3aISjg8S3nKk4-eAg"));
                results.add(new InlineQueryResultCachedVoice().setTitle("Жадина говядина").setId("zadina").setVoiceFileId("AwADAgADjwADV3aQSu4lCxyRlGYFAg"));
                results.add(new InlineQueryResultCachedVoice().setTitle("Ахахха").setId("lol").setVoiceFileId("AwADAgADkAADV3aQSrzEVg0uNSExAg"));
            }
        }
        else {
            InlineQueryResultArticle nothing = new InlineQueryResultArticle()
                    .setInputMessageContent(new InputTextMessageContent().setMessageText(""))
                    .setTitle("There is nothing here").setId("Nothing");
            results.add(nothing);
        }
        answer.setResults(results);
        try {
            execute(answer);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    @Override
    public String getBotToken() {
        return "498067716:AAFIwfyWSyuubeuNqxMKCpw_JJL0yMjC-rY";
    }

    private void dispath(String text, boolean silence) {
        SendMessage m = new SendMessage();
        if(silence) m.disableNotification();
        for (User user: users) {
            m.setChatId(user.getId());
            m.setText(text);
            try {
                execute(m);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMsgToMainChat(String text) {
        SendMessage m = new SendMessage();
        m.setText(text);
        m.setChatId("-1001098446715");
        try {
            execute(m);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void createPoll(Long chatId){
        SendMessage m = new SendMessage();
        sumAttack = 0;
        editedLine = "Проверка готовности!\nСуммарная атака: " + sumAttack;
        m.setChatId(chatId);
        InlineKeyboardMarkup markupInline1 = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Жмяк").setCallbackData("update_msg_text"));
        rowsInline.add(rowInline);
        markupInline1.setKeyboard(rowsInline);
        markupInline = markupInline1;
        m.setReplyMarkup(markupInline);
        m.setText("Проверка готовности!");
        try {
            execute(m);
        }
        catch (Exception e) {e.getMessage();}
    }

    private void createReport(Message msg){
        SendMessage m = new SendMessage();
        m.setChatId(msg.getChatId());
        InlineKeyboardMarkup markupInlineReport = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Report!").setSwitchInlineQuery("Репорт!"));
        rowsInline.add(rowInline);
        markupInlineReport.setKeyboard(rowsInline);
        m.setReplyMarkup(markupInlineReport);
        m.setText("Кидайте репорты!");
        try {
            execute(m);
        }
        catch (Exception e) {e.getMessage();}
    }

    private void postToChanel(Message msg){
        try {
            execute(new ForwardMessage()
                    .setFromChatId(msg.getChatId())
                    .setMessageId(msg.getReplyToMessage().getMessageId())
                    .setChatId("-1001117576309"));
            try{
                execute(new SendMessage()
                        .setText("Ахахах, дааа! Кстати, смотри больше на канале: https://t.me/kartaviikarapyliandmore")
                        .setChatId(msg.getChatId()).setReplyToMessageId(msg.getMessageId()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
