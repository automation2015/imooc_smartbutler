package auto.cn.smartbutler.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.bean.ChatMsg;

public class ChatMsgAdapter extends BaseAdapter {
	private List<ChatMsg> mDatas;
	private LayoutInflater mInflater;
	
	public ChatMsgAdapter(Context context,List<ChatMsg> mData) {
		this.mDatas=mData;
		 mInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		ChatMsg chatMsg = mDatas.get(position);
		if (chatMsg.getType()==ChatMsg.Type.INCOMING) {
			return 0;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		ChatMsg chatMsg=mDatas.get(position);
		if (convertView==null) {
			if (getItemViewType(position)==0) {
				convertView=mInflater.inflate(R.layout.item_from_msg, parent,false);
				viewHolder=new ViewHolder();
				viewHolder.mMsg=(TextView) convertView.findViewById(R.id.tvFromMsgInfo);
				viewHolder.mDate=(TextView) convertView.findViewById(R.id.tvFromMsgDate);
//			    convertView.setTag(viewHolder);
			}else{
				convertView=mInflater.inflate(R.layout.item_to_msg, parent,false);
				viewHolder=new ViewHolder();
				viewHolder.mMsg=(TextView) convertView.findViewById(R.id.tvToMsgInfo);
				viewHolder.mDate=(TextView) convertView.findViewById(R.id.tvToMsgDate);
			}
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.mMsg.setText(chatMsg.getMsg());
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.format(chatMsg.getDate());
		viewHolder.mDate.setText(df.format(chatMsg.getDate()));
		return convertView;
	}
	private final class ViewHolder{
		TextView  mMsg,mDate;
	}
}
