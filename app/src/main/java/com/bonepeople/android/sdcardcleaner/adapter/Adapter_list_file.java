package com.bonepeople.android.sdcardcleaner.adapter;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonepeople.android.sdcardcleaner.R;
import com.bonepeople.android.sdcardcleaner.models.SDFile;
import com.bonepeople.android.sdcardcleaner.utils.NumberUtil;

/**
 * 文件列表的数据适配器
 * <p>
 * Created by bonepeople on 2017/6/26.
 */

public class Adapter_list_file extends RecyclerView.Adapter<Adapter_list_file.ViewHolder> {
    public static final String ACTION_CLICK_ITEM = "click_item";
    private static final int COLOR_START = 0xFFEAD799;
    private static final int COLOR_END = 0xFFE96E3E;
    private static final ArgbEvaluator _evaluator = new ArgbEvaluator();
    private Context _context;
    private SDFile _data;
    private View.OnClickListener _listener_click;
    private View.OnLongClickListener _listener_long;
    private boolean _multiSelect = false;

    public Adapter_list_file(Context _context, View.OnClickListener _listener_click, View.OnLongClickListener _listener_long) {
        this._context = _context;
        this._listener_click = _listener_click;
        this._listener_long = _listener_long;
    }

    public void set_data(SDFile _data) {
        this._data = _data;
    }

    public SDFile get_data() {
        return _data;
    }

    public void set_multiSelect(boolean _multiSelect) {
        this._multiSelect = _multiSelect;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View _view = LayoutInflater.from(_context).inflate(R.layout.item_list_file, parent, false);
        return new ViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SDFile _temp_data = _data.get_children().get(position);
        //设置文件大小比例条
        float _percent = (float) NumberUtil.div(_temp_data.get_sizePercent(), 100, 2);
        PercentRelativeLayout.LayoutParams _params = new PercentRelativeLayout.LayoutParams(0, 0);
        _params.getPercentLayoutInfo().widthPercent = _percent;
        holder._view_percent.setLayoutParams(_params);
        int _color = (int) _evaluator.evaluate(_percent, COLOR_START, COLOR_END);
        holder._view_percent.setBackgroundColor(_color);
        //设置复选框状态
        if (_multiSelect) {
            if (holder._checkbox.getVisibility() == CheckBox.GONE)
                holder._checkbox.setVisibility(CheckBox.VISIBLE);
        } else {
            if (holder._checkbox.getVisibility() == CheckBox.VISIBLE)
                holder._checkbox.setVisibility(CheckBox.GONE);
        }

        //设置类型图标
        if (_temp_data.isDirectory())
            holder._image_type.setImageResource(R.drawable.icon_directory);
        else
            holder._image_type.setImageResource(R.drawable.icon_file);
        //设置基本信息
        holder._text_name.setText(_temp_data.get_name());
        holder._text_size.setText(Formatter.formatFileSize(_context, _temp_data.get_size()));
        holder._view_click.setTag(new String[]{ACTION_CLICK_ITEM, String.valueOf(position)});
    }

    @Override
    public int getItemCount() {
        return _data.get_children().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View _view_percent;
        public CheckBox _checkbox;
        public ImageView _image_type;
        public TextView _text_name;
        public TextView _text_size;
        public View _view_click;

        public ViewHolder(View itemView) {
            super(itemView);
            _view_percent = itemView.findViewById(R.id.view_percent);
            _checkbox = (CheckBox) itemView.findViewById(R.id.checkbox_item);
            _image_type = (ImageView) itemView.findViewById(R.id.imageview_type);
            _text_name = (TextView) itemView.findViewById(R.id.textview_name);
            _text_size = (TextView) itemView.findViewById(R.id.textview_size);
            _view_click = itemView.findViewById(R.id.view_click);
            _view_click.setOnClickListener(_listener_click);
            _view_click.setOnLongClickListener(_listener_long);
        }
    }
}
