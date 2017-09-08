package manhnd.com.demoemotiongif;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;

import manhnd.com.demoemotiongif.databinding.ActivityMainBinding;
import manhnd.com.demoemotiongif.emoticonMessenger.MessengerEmoticonProvider;
import manhnd.com.demoemotiongif.giphy.GiphyGifProvider;
import manhnd.com.demoemotiongif.keyboard.EmoticonGIFKeyboardFragment;
import manhnd.com.demoemotiongif.keyboard.emoticons.Emoticon;
import manhnd.com.demoemotiongif.keyboard.emoticons.EmoticonSelectListener;
import manhnd.com.demoemotiongif.keyboard.gifs.Gif;
import manhnd.com.demoemotiongif.keyboard.gifs.GifSelectListener;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;

    private static final String TAG = "MainActivity";
    private EmoticonGIFKeyboardFragment mEmoticonGIFKeyboardFragment;

    /**
     * Manually toggle soft keyboard visibility
     *
     * @param context calling context
     */
    public static void toggleKeyboardVisibility(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainBinding.selectedEmoticonsTv.setEmoticonProvider(MessengerEmoticonProvider.create());

        mainBinding.selectedEmoticonsEt.setEmoticonProvider(MessengerEmoticonProvider.create());

        EmoticonGIFKeyboardFragment.EmoticonConfig emoticonConfig = new EmoticonGIFKeyboardFragment.EmoticonConfig()
                .setEmoticonProvider(MessengerEmoticonProvider.create())
                .setEmoticonSelectListener(new EmoticonSelectListener() {

                    @Override
                    public void emoticonSelected(Emoticon emoticon) {
                        Log.d(TAG, "emoticonSelected: " + emoticon.getUnicode());
                        mainBinding.selectedEmoticonsEt.append(emoticon.getUnicode(),
                                mainBinding.selectedEmoticonsEt.getSelectionStart(),
                                mainBinding.selectedEmoticonsEt.getSelectionEnd());
                    }

                    @Override
                    public void onBackSpace() {
                    }
                });


        EmoticonGIFKeyboardFragment.GIFConfig gifConfig = new EmoticonGIFKeyboardFragment
                .GIFConfig(GiphyGifProvider.create(this, "564ce7370bf347f2b7c0e4746593c179"))

                .setGifSelectListener(new GifSelectListener() {
                    @Override
                    public void onGifSelected(@NonNull Gif gif) {
                        //Do something with the selected GIF.
                        Log.d(TAG, "onGifSelected: " + gif.getGifUrl());
                        Glide.with(MainActivity.this)
                                .load(gif.getGifUrl())
                                .asGif()
                                .placeholder(R.mipmap.ic_launcher)
                                .into(mainBinding.selectedGitIv);
                    }
                });


        mEmoticonGIFKeyboardFragment = EmoticonGIFKeyboardFragment
                .getNewInstance(findViewById(R.id.keyboard_container), emoticonConfig, gifConfig);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.keyboard_container, mEmoticonGIFKeyboardFragment)
                .commit();
        mEmoticonGIFKeyboardFragment.open();
        findViewById(R.id.emoji_open_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmoticonGIFKeyboardFragment.toggle();
                toggleKeyboardVisibility(MainActivity.this);
            }
        });

        mainBinding.emojiSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBinding.selectedEmoticonsTv.append(mainBinding.selectedEmoticonsEt.getText());
                mainBinding.selectedEmoticonsEt.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mEmoticonGIFKeyboardFragment == null || !mEmoticonGIFKeyboardFragment.handleBackPressed())
            super.onBackPressed();
    }
}
