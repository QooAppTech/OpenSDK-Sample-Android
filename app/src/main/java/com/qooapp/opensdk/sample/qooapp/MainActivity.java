package com.qooapp.opensdk.sample.qooapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qooapp.opensdk.QooAppOpenSDK;
import com.qooapp.opensdk.common.PaymentCallback;
import com.qooapp.opensdk.common.QooAppCallback;
import com.qooapp.opensdk.sample.qooapp.model.OrderBean;
import com.qooapp.opensdk.sample.qooapp.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private View mLayoutFunction;

    private ListView mListView;
    private long mBackKeyTime = 0;

    private ProgressDialog progressDialog;

    private List<Product> mProductsList = new ArrayList<>();
    private List<OrderBean> mOrdersList = new ArrayList<>();

    private final int TYPE_ERROR = 0;

    private final int TYPE_LOGIN = 1;

    private final int TYPE_REWARD = 2;

    private final int TYPE_VERIFY = 3;

    private final int TYPE_QUERY_PRODUCT = 4;

    private final int TYPE_QUERY_RECORD = 5;

    private PaymentCallback mPaymentCallback = new PaymentCallback() {
        @Override
        public void onComplete(String json) {

            //Handle success case
            try {
                /**
                 * 1、purchase success，then please distribute goods to player.
                 * 2、When player get product, you must call consumePurchase();
                 */
                JSONObject obj = new JSONObject(json);
                JSONObject jsonObject = obj.getJSONObject("data");

                Gson gson = new Gson();
                OrderBean orderBean = gson.fromJson(jsonObject.toString(), OrderBean.class);
                showToast("Purchasing successful，Consuming...[purchase_id:" + orderBean.getPurchase_id() + ",token:" + orderBean.getToken());
                showPaymentDialog((dialog, which) -> {
                    showProgress();
                    consumePurchase(orderBean.getPurchase_id(), orderBean.getToken());
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {
            showToast("Error:" + error);
        }

        @Override
        public void onCancel() {
            showToast("Be canceled");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("QooAppOpenSDK: Initialize  v"+BuildConfig.VERSION_NAME);

        initView();
    }

    private void initView() {
        mLayoutFunction = this.findViewById(R.id.sv_function);
        mListView = this.findViewById(R.id.list_view);
        this.findViewById(R.id.btn_init).setOnClickListener(v -> {

            showProgress();
            // you can use this way to init QooAppOpenSDK.
            // you must provide params in AndroidManifest.xml
            QooAppOpenSDK.initialize(new QooAppCallback() {
                @Override
                public void onSuccess(String response) {
                    displayResult(TYPE_LOGIN, response);
                }

                @Override
                public void onError(String error) {
                    displayResult(TYPE_ERROR, error);
                }
            }, this);

        });

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            try {
                QooAppOpenSDK.getInstance().logout(new QooAppCallback() {
                    @Override
                    public void onSuccess(String response) {
                        showToast(response);
                    }

                    @Override
                    public void onError(String error) {
                        showToast(error);

                    }
                }, MainActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
                showToast(e.getMessage());
            }
        });

        findViewById(R.id.btn_check_reward).setOnClickListener(v -> {
            showProgress();
            QooAppOpenSDK.getInstance().checkReward(new QooAppCallback() {

                @Override
                public void onSuccess(String info) {
                    // verification succeed
                    displayResult(TYPE_REWARD, info);
                }

                @Override
                public void onError(String error) {
                    // For unknown reason, verification cannot be done.
                    // Please disallow access for proper protection.
                    displayResult(TYPE_ERROR, error);
                }
            });
        });

        findViewById(R.id.btn_verify).setOnClickListener(v -> {
            showProgress();
            QooAppOpenSDK.getInstance().checkLicense(new QooAppCallback() {

                @Override
                public void onSuccess(String info) {
                    // verification succeed
                    displayResult(TYPE_VERIFY, info);
                }

                @Override
                public void onError(String error) {
                    // For unknown reason, verification cannot be done.
                    // Please disallow access for proper protection.
                    displayResult(TYPE_VERIFY, error);
                }
            });
        });

        findViewById(R.id.btn_products).setOnClickListener(v -> {
            showProgress();
            QooAppOpenSDK.getInstance().queryProducts(new QooAppCallback() {
                @Override
                public void onSuccess(String result) {
                    displayResult(TYPE_QUERY_PRODUCT, result);
                }

                @Override
                public void onError(String error) {
                    displayResult(TYPE_ERROR, error);
                }
            });
        });

        this.findViewById(R.id.btn_purchased).setOnClickListener(v -> {
            showProgress();
            QooAppOpenSDK.getInstance().restorePurchases(new QooAppCallback() {
                @Override
                public void onSuccess(String result) {
                    hideProgress();
                    displayResult(TYPE_QUERY_RECORD, result);
                }

                @Override
                public void onError(String error) {
                    hideProgress();
                    displayResult(TYPE_ERROR, error);
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void consumePurchase(String purchase_id, String token) {
        QooAppOpenSDK.getInstance().consume(new QooAppCallback() {
            @Override
            public void onSuccess(String response) {
                showToast("Consumption successful!");
                Log.d("QooAppOpenSDK", "response = "+response);
                hideProgress();
                QooAppOpenSDK.getInstance().closePaymentUI();
            }

            @Override
            public void onError(String error) {
                Log.e("QooAppOpenSDK", "error = "+error);
                hideProgress();
                showToast("Consuming error:" + error);
            }
        }, purchase_id, token);
    }

    /**
     * show toast info
     * @param text
     */
    private void showToast(CharSequence text) {
        Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * @param type which type info
     * @param result
     */
    private void displayResult(final int type, final String result) {
        Log.d(TAG,"type = "+type+", result = "+result);
        hideProgress();
        showDialog(result, (dialog, which) -> {
            switch (type) {
                case TYPE_LOGIN:
                    showFunctionView();
                    break;
                case TYPE_QUERY_PRODUCT:
                    parseProducts(result);
                    break;
                case TYPE_QUERY_RECORD:
                    parseRecords(result);
                    break;
            }
        });
    }

    /**
     * parse products list info
     * @param result
     */
    private void parseProducts(String result) {
        Log.d(TAG, "result："+result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            if (dataArray != null) {
                mProductsList.clear();
                mProductsList = parseString2List(dataArray.toString(), Product.class);

                ProductAdapter adapter = new ProductAdapter(this, mProductsList);
                mListView.setVisibility(View.VISIBLE);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener((parent, view, position, id) -> {
                    final Product product = mProductsList.get(position);
                    QooAppOpenSDK.getInstance().purchase(mPaymentCallback, MainActivity.this, product.getProduct_id(), "cporderid----", "dev-0110");
                });
                showProducts("Products");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    /**
     * parse records list
     * @param result
     */
    private void parseRecords(String result) {
        try {

            JSONObject obj = new JSONObject(result);
            JSONArray dataArray = obj.getJSONArray("data");

            if (dataArray != null) {
                mOrdersList = parseString2List(dataArray.toString(), OrderBean.class);
                OrdersAdapter adapter = new OrdersAdapter(this, mOrdersList);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener((parent, view, position, id) -> {
                    final OrderBean orderBean = mOrdersList.get(position);
                    showDialog("consume this order? ",  (dialog, which) -> {
                        showProgress();
                        consumePurchase(orderBean.getPurchase_id(), orderBean.getToken());
                    });
                });
                showProducts("Records");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    private void showFunctionView() {
        setTitle("functions");
        mLayoutFunction.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    private void showProducts(String title) {
        setTitle(title);
        mLayoutFunction.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
        }
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mListView.getVisibility() == View.VISIBLE) {
            showFunctionView();
        } else if (System.currentTimeMillis() - mBackKeyTime < 2000){
            finish();
        } else {
            mBackKeyTime = System.currentTimeMillis();
        }
    }

    private void showDialog(String title, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        if (title != null) {
            builder.setMessage(title);
        }
        builder.setPositiveButton("Ok", listener);
        AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.show();
    }

    private void showPaymentDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("After the payment is successful, the goods need to be distributed. After the goods are successfully distributed, QooAppOpenSDK.getInstance().consume() needs to be called");
        builder.setPositiveButton("consume", listener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.show();
    }

    /**
     * @return
     */
    private  <T> List<T> parseString2List(String json, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list =  new Gson().fromJson(json, type);
        return list;
    }

    class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}