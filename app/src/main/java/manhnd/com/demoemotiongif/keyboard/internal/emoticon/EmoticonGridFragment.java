package manhnd.com.demoemotiongif.keyboard.internal.emoticon;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ViewFlipper;

import java.util.List;

import manhnd.com.demoemotiongif.R;
import manhnd.com.demoemotiongif.keyboard.emoticons.Emoticon;
import manhnd.com.demoemotiongif.keyboard.emoticons.EmoticonProvider;
import manhnd.com.demoemotiongif.keyboard.emoticons.EmoticonSelectListener;

/**
 * A simple {@link Fragment} subclass.
 */
public final class EmoticonGridFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_CATEGORY_ID = "arg_category_id";

    /**
     * Listener to notify when emoticons selected.
     */
    @Nullable
    private EmoticonSelectListener mEmoticonSelectListener;

    /**
     * Emoticon provider
     */
    @Nullable
    private EmoticonProvider mEmoticonProvider;


    public EmoticonGridFragment() {
        // Required empty public constructor
    }


    static EmoticonGridFragment newInstance(@EmoticonsCategories.EmoticonsCategory int category,
                                            @Nullable EmoticonSelectListener emoticonSelectListener,
                                            @Nullable EmoticonProvider emoticonProvider) {
        EmoticonGridFragment emoticonGridFragment = new EmoticonGridFragment();
        emoticonGridFragment.setEmoticonProvider(emoticonProvider);
        emoticonGridFragment.setEmoticonSelectListener(emoticonSelectListener);

        //Set the category id arguments
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, category);
        emoticonGridFragment.setArguments(bundle);

        return emoticonGridFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emoticon_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewFlipper mainViewFlipper = view.findViewById(R.id.recent_emoticon_flipper);

        List<Emoticon> emoticonList = getEmoticonsList(getArguments().getInt(ARG_CATEGORY_ID, -1));

        if (emoticonList.isEmpty()) {
            mainViewFlipper.setDisplayedChild(1);
        } else {
            mainViewFlipper.setDisplayedChild(0);

            GridView emoticonGrid = view.findViewById(R.id.emoticon_list_grid);
            emoticonGrid.setNumColumns(getResources().getInteger(R.integer.emoticon_recycler_view_span_size));
            emoticonGrid.setOnItemClickListener(this);

            //Bind the adapter
            //noinspection WrongConstant
            EmoticonGridAdapter emoticonGridAdapter = new EmoticonGridAdapter(getActivity(),
                    mEmoticonProvider, emoticonList);
            emoticonGrid.setAdapter(emoticonGridAdapter);
        }
    }

    /**
     * Set the {@link EmoticonSelectListener} to get notify whenever the emoticon is selected or deleted.
     *
     * @param emoticonSelectListener {@link EmoticonSelectListener}
     */
    @SuppressWarnings("ConstantConditions")
    public void setEmoticonSelectListener(@Nullable EmoticonSelectListener emoticonSelectListener) {
        mEmoticonSelectListener = emoticonSelectListener;
    }

    /**
     * Set the {@link EmoticonProvider} to render different images for unicode. If the value is null,
     * system emoticon images will render.
     *
     * @param emoticonProvider {@link EmoticonProvider}
     */
    public void setEmoticonProvider(@Nullable EmoticonProvider emoticonProvider) {
        mEmoticonProvider = emoticonProvider;
    }

    /**
     * Get the emoticons list for the selected category.
     *
     * @param category category id.
     * @return List of {@link Emoticon}
     */
    private List<Emoticon> getEmoticonsList(@EmoticonsCategories.EmoticonsCategory int category) {
        switch (category) {
            case EmoticonsCategories.RECENT:
                return EmoticonRecentManager.getInstance(getActivity()).getRecentEmoticons();
            case EmoticonsCategories.PEOPLE:
            case EmoticonsCategories.NATURE:
            case EmoticonsCategories.FOOD:
            case EmoticonsCategories.ACTIVITY:
            case EmoticonsCategories.TRAVEL:
            case EmoticonsCategories.OBJECTS:
            case EmoticonsCategories.SYMBOLS:
            case EmoticonsCategories.FLAGS:
                return new EmoticonDbHelper(getActivity()).getEmoticons(category, mEmoticonProvider);
            default:
                throw new IllegalStateException("Invalid position.");
        }
    }

    /**
     * When any of the item from the emoticon grid will click, this method will call.
     *
     * @param position Position if the item clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Emoticon emoticon = (Emoticon) adapterView.getAdapter().getItem(position);

        //Notify the emoticon
        if (mEmoticonSelectListener != null)
            mEmoticonSelectListener.emoticonSelected(emoticon);

        //Save the emoticon to the recent list
        EmoticonRecentManager.getInstance(getActivity()).add(emoticon);
    }
}
