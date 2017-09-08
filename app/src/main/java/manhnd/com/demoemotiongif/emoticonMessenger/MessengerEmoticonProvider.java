package manhnd.com.demoemotiongif.emoticonMessenger;

import manhnd.com.demoemotiongif.keyboard.emoticons.EmoticonProvider;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 07/09/2017.
 */

public class MessengerEmoticonProvider implements EmoticonProvider {

    private MessengerEmoticonProvider() {
    }

    /**
     * return {@link MessengerEmoticonProvider}
     */
    public static MessengerEmoticonProvider create() {
        return new MessengerEmoticonProvider();
    }

    /**
     * Get the drawable resource for the given unicode.
     *
     * @param unicode Unicode for which icon is required.
     * @return Icon drawable resource id or -1 if there is no drawable for given unicode.
     */
    @Override
    public int getIcon(String unicode) {
        return hasEmoticonIcon(unicode) ? EmoticonList.EMOTICONS.get(unicode) : -1;
    }

    /**
     * Check if the icon pack contains the icon image for given unicode/emoticon?
     *
     * @param unicode Unicode to check.
     * @return True if the icon found else false.
     */
    @Override
    public boolean hasEmoticonIcon(String unicode) {
        return EmoticonList.EMOTICONS.containsKey(unicode);
    }
}
