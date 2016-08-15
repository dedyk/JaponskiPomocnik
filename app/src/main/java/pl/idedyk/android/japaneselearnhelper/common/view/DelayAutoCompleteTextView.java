package pl.idedyk.android.japaneselearnhelper.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;

public class DelayAutoCompleteTextView extends AutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int AUTOCOMPLETE_DELAY = 1;

    private CheckBox useAutocompleteCheckBox;
    
    @SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
    	
        @Override
        public void handleMessage(Message msg) {
        	
        	MessageObject messageObject = (MessageObject)msg.obj;
        	
            DelayAutoCompleteTextView.super.performFiltering(messageObject.getText(), messageObject.getKeyCode());
        }
    };

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
    	
    	if (useAutocompleteCheckBox != null && useAutocompleteCheckBox.isChecked() == true) {
    	        
	        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
	        
	        Message message = mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, new MessageObject(text, keyCode));
	        
	        mHandler.sendMessageDelayed(message, AUTOCOMPLETE_DELAY);
    	}
    }

    @Override
    public void onFilterComplete(int count) {
    	super.onFilterComplete(count);
    }
    
    public void setUseAutocompleteCheckBox(CheckBox useAutocompleteCheckBox) {
    	this.useAutocompleteCheckBox = useAutocompleteCheckBox;
    }
    
    private static class MessageObject {
    	
    	public MessageObject(CharSequence text, int keyCode) {
    		this.text = text;
    		this.keyCode = keyCode;
    	}
    	
    	private CharSequence text;
    	
    	private int keyCode;

		public CharSequence getText() {
			return text;
		}

		public int getKeyCode() {
			return keyCode;
		}
    }
}
