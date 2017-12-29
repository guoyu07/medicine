package com.yangs.medicine.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.melnykov.fab.FloatingActionButton;
import com.yangs.medicine.R;
import com.yangs.medicine.activity.APPlication;
import com.yangs.medicine.adapter.TaskAdapter;
import com.yangs.medicine.model.TaskList;
import com.yangs.medicine.source.QuestionSource;
import com.yangs.medicine.util.TaskOperateUntil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangs on 2017/10/4 0004.
 */

public class TaskFragment extends LazyLoadFragment implements TaskAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View mLay;
    private SwipeRefreshLayout srl;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<TaskList> lists;
    private TaskAdapter taskAdapter;
    private TextView empty_tv;
    private int c = 0;
    private String type;
    private AlertDialog taskdialog;
    private Handler handler;

    @Override
    protected int setContentView() {
        return R.layout.taskfrag_layout;
    }

    @Override
    protected void lazyLoad() {
        if (isInit) {
            if (!isLoad) {
                initView();
            }
        }
    }

    private void initView() {
        handler = new Handler();
        lists = new ArrayList<>();
        mLay = getContentView();
        fab = (FloatingActionButton) mLay.findViewById(R.id.task_fab);
        srl = (SwipeRefreshLayout) mLay.findViewById(R.id.task_srl);
        recyclerView = (RecyclerView) mLay.findViewById(R.id.task_rv);
        if (type == null) {
            fab.setVisibility(View.VISIBLE);
            fab.attachToRecyclerView(recyclerView);
            fab.setOnClickListener(this);
            type = "未完成";
        }
        taskAdapter = new TaskAdapter(getContext(), lists, type);
        empty_tv = (TextView) mLay.findViewById(R.id.task_tv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskAdapter.setOnItemClickListener(this);
        srl.setColorSchemeColors(Color.CYAN, Color.GREEN, ContextCompat.getColor(getContext(),
                R.color.colorPrimary));
        recyclerView.setAdapter(taskAdapter);
        srl.setOnRefreshListener(this);
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(true);
                onRefresh();
            }
        });
        fab.setImageResource(R.drawable.ic_edit_white);
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void onItemClick(final int position) {
        if (type.equals("我发布的")) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.taskoperatedialog_layout, null);
            final AlertDialog task_operate_dialog = new AlertDialog.Builder(getContext()).setView(view).setCancelable(false).create();
            ImageView iv_close = (ImageView) view.findViewById(R.id.taskoperatedialog_iv_close);
            TextView tv_task_operate_tv = (TextView) view.findViewById(R.id.taskoperatedialog_tv);
            Button bt_finish = (Button) view.findViewById(R.id.taskoperatedialog_bt_finish);
            Button bt_notaccept = (Button) view.findViewById(R.id.taskoperatedialog_bt_notaccept);
            Button bt_deltask = (Button) view.findViewById(R.id.taskoperatedialog_bt_deltask);
            final TaskList taskList = lists.get(position);
            if (taskList.getStatus().equals("未接受"))
                tv_task_operate_tv.setText("状态:   " + taskList.getStatus() + "\n"
                        + "发布时间:   " + taskList.getTime());
            else
                tv_task_operate_tv.setText("状态:   " + taskList.getStatus() + "\n"
                        + "发布时间:   " + taskList.getTime() + "\n"
                        + "接单人:   " + taskList.getAccepter() + "\n"
                        + "接单时间:   " + taskList.getAccepttime());
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (task_operate_dialog != null)
                        task_operate_dialog.cancel();
                }
            });
            bt_deltask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext()).setTitle("提示")
                            .setMessage("你确定要删除这个任务?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            TaskOperateUntil.doTaskByID("delTaskByID", getContext(), handler, taskList.getId(),
                                    new TaskOperateUntil.OnResultListener() {
                                        @Override
                                        public void onResult(int code) {
                                            switch (code) {
                                                case -1:
                                                    APPlication.showToast("网络出错", 0);
                                                    break;
                                                case 1:
                                                    APPlication.showToast("删除成功", 0);
                                                    task_operate_dialog.dismiss();
                                                    srl.setRefreshing(true);
                                                    onRefresh();
                                                    break;
                                                case 0:
                                                    APPlication.showToast("删除失败,请反馈!\n" +
                                                            "id: " + taskList.getId(), 1);
                                                    break;
                                            }
                                        }
                                    });
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            });
            bt_notaccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskOperateUntil.acceptTaskByID(getContext(), handler, "cancel", APPlication.user,
                            lists.get(position).getId(), new TaskOperateUntil.OnResultListener() {
                                @Override
                                public void onResult(int code) {
                                    switch (code) {
                                        case -1:
                                            APPlication.showToast("网络出错", 0);
                                            break;
                                        case 1:
                                            APPlication.showToast("标记成功", 0);
                                            task_operate_dialog.dismiss();
                                            srl.setRefreshing(true);
                                            onRefresh();
                                            break;
                                        case 0:
                                            APPlication.showToast("标记失败,请反馈!\n" +
                                                    "id: " + lists.get(position).getId(), 1);
                                            break;
                                    }
                                }
                            });
                }
            });
            bt_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskOperateUntil.doTaskByID("finishTaskByID", getContext(), handler, taskList.getId(),
                            new TaskOperateUntil.OnResultListener() {
                                @Override
                                public void onResult(int code) {
                                    switch (code) {
                                        case -1:
                                            APPlication.showToast("网络出错", 0);
                                            break;
                                        case 1:
                                            APPlication.showToast("标记成功", 0);
                                            task_operate_dialog.dismiss();
                                            srl.setRefreshing(true);
                                            onRefresh();
                                            break;
                                        case 0:
                                            APPlication.showToast("标记失败,请反馈!\n" +
                                                    "id: " + taskList.getId(), 1);
                                            break;
                                    }
                                }
                            });
                }
            });
            task_operate_dialog.show();
            return;
        }
        if (type.equals("我接受的")) {
            if (lists.get(position).getStatus().equals("待完成")) {
                new AlertDialog.Builder(getContext()).setTitle("提示")
                        .setMessage("\n完成这个任务以后,请联系发布者,让他标记为已完成")
                        .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("我要取消任务", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new AlertDialog.Builder(getContext()).setTitle("提示")
                                .setMessage("你确定要取消这个任务吗?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        TaskOperateUntil.acceptTaskByID(getContext(), handler, "cancel", APPlication.user,
                                                lists.get(position).getId(), new TaskOperateUntil.OnResultListener() {
                                                    @Override
                                                    public void onResult(int code) {
                                                        switch (code) {
                                                            case -1:
                                                                APPlication.showToast("网络出错", 0);
                                                                break;
                                                            case 1:
                                                                APPlication.showToast("取消成功", 0);
                                                                srl.setRefreshing(true);
                                                                onRefresh();
                                                                break;
                                                            case 0:
                                                                APPlication.showToast("取消失败,请反馈!\n" +
                                                                        "id: " + lists.get(position).getId(), 1);
                                                                break;
                                                        }
                                                    }
                                                });
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                }).create().show();
            } else
                APPlication.showToast("这个任务已经完成啦", 0);
            return;
        }
        if (lists.get(position).getUser().equals(APPlication.user)) {
            new AlertDialog.Builder(getContext()).setTitle("不能接受自己的任务哦")
                    .setMessage("\n可以在我的-我的任务里面删除此任务")
                    .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
            return;
        }
        if (lists.get(position).getStatus().equals("待完成")) {
            if (lists.get(position).getAccepter().equals(APPlication.user)) {
                new AlertDialog.Builder(getContext()).setTitle("你已经接下了这个任务")
                        .setMessage("请联系发布者完成任务\n在我的-我的任务里面可以查看,取消任务!")
                        .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            } else {
                new AlertDialog.Builder(getContext()).setTitle("这个任务已经被人接受了")
                        .setMessage("接单人:   " + lists.get(position).getAccepter()
                                + "\n接单时间:   " + lists.get(position).getAccepttime())
                        .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            return;
        }
        AlertDialog accept_dialog = new AlertDialog.Builder(getContext()).setTitle("任务")
                .setMessage("您确定要接受这个任务吗？\n\n" + lists.get(position).getContent())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TaskOperateUntil.acceptTaskByID(getContext(), handler, "accept", APPlication.user,
                                lists.get(position).getId(), new TaskOperateUntil.OnResultListener() {
                                    @Override
                                    public void onResult(int code) {
                                        switch (code) {
                                            case -1:
                                                APPlication.showToast("网络出错", 0);
                                                break;
                                            case 1:
                                                APPlication.showToast("接受成功", 0);
                                                srl.setRefreshing(true);
                                                onRefresh();
                                                break;
                                            case 0:
                                                APPlication.showToast("接受失败,请反馈!\n" +
                                                        "id: " + lists.get(position).getId(), 1);
                                                break;
                                        }
                                    }
                                });
                    }
                }).create();
        accept_dialog.show();
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPlication.questionSource.getTaskList(type, APPlication.user, 0 + "",
                        new QuestionSource.OnResponseCodeResultListener() {
                            @Override
                            public void onResponseResult(int code, String response) {
                                if (code == -1) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            srl.setRefreshing(false);
                                            APPlication.showToast("网络出错", 0);
                                        }
                                    });
                                    return;
                                }
                                JSONObject jsonObject = JSON.parseObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("TaskList");
                                lists.clear();
                                for (int i = 0, j = jsonArray.size(); i < j; i++) {
                                    JSONObject jo = (JSONObject) jsonArray.get(i);
                                    TaskList taskList = new TaskList();
                                    taskList.setPicUrl("res://com.yangs.medicine/" + R.drawable.img_oldsong);
                                    taskList.setId(jo.getString("id"));
                                    taskList.setContent(jo.getString("content"));
                                    taskList.setMoney(jo.getString("money") + "¥");
                                    taskList.setTime(jo.getString("time"));
                                    taskList.setUser(jo.getString("user"));
                                    taskList.setStatus(jo.getString("status"));
                                    taskList.setAccepter(jo.getString("accepter"));
                                    taskList.setAccepttime(jo.getString("accepttime"));
                                    taskList.setFinishtime(jo.getString("finishtime"));
                                    lists.add(taskList);
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (lists.size() == 0) {
                                            empty_tv.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.GONE);
                                        } else {
                                            empty_tv.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                        }
                                        srl.setRefreshing(false);
                                        taskAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_fab:
                if (taskdialog == null) {
                    View view = LayoutInflater.from(getContext()).inflate(
                            R.layout.taskdialog_layout, null);
                    ImageView iv_close = (ImageView) view.findViewById(R.id.taskdialog_iv_close);
                    final EditText et_content = (EditText) view.findViewById(R.id.taskdialog_et_content);
                    final EditText et_money = (EditText) view.findViewById(R.id.taskdialog_et_money);
                    final ProgressBar dis_pb = (ProgressBar) view.findViewById(R.id.taskdialog_pb);
                    Button bt_sub = (Button) view.findViewById(R.id.taskdialog_bt_sub);
                    taskdialog = new AlertDialog.Builder(getContext()).setCancelable(false)
                            .setView(view).create();
                    iv_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (taskdialog != null)
                                taskdialog.cancel();
                        }
                    });
                    bt_sub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String content = et_content.getText().toString().trim();
                            final String money = et_money.getText().toString().trim();
                            if ("".equals(content)) {
                                APPlication.showToast("请输入要求", 0);
                                return;
                            }
                            if ("".equals(money)) {
                                APPlication.showToast("请输入金额", 0);
                                return;
                            }
                            dis_pb.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    APPlication.questionSource.addTask(content, APPlication.user, money,
                                            new QuestionSource.OnResponseCodeResultListener() {
                                                @Override
                                                public void onResponseResult(int code, String response) {
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dis_pb.setVisibility(View.GONE);
                                                        }
                                                    });
                                                    if (code == -1) {
                                                        APPlication.showToast("网络出错!", 0);
                                                        return;
                                                    }
                                                    if (response.equals("成功")) {
                                                        APPlication.showToast("发布成功", 0);
                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                taskdialog.dismiss();
                                                                srl.setRefreshing(true);
                                                                onRefresh();
                                                            }
                                                        });
                                                    } else
                                                        APPlication.showToast("发布失败,请反馈!", 1);
                                                }
                                            });
                                }
                            }).start();
                        }
                    });
                }
                taskdialog.show();
                break;
        }
    }
}
