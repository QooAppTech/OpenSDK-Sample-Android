package com.qooapp.opensdk.sample.qooapp;


import static android.util.Base64.DEFAULT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qooapp.opensdk.QooAppOpenSDK;
import com.qooapp.opensdk.common.PaymentCallback;
import com.qooapp.opensdk.common.QooAppCallback;
import com.qooapp.opensdk.common.model.SDKLocales;
import com.qooapp.opensdk.sample.qooapp.model.OrderBean;
import com.qooapp.opensdk.sample.qooapp.model.PageProductResponse;
import com.qooapp.opensdk.sample.qooapp.model.Product;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private View mLayoutFunction;

    private EditText mEdtLanguage;

    private ListView mListView;
    private long mBackKeyTime = 0;

    private ProgressDialog progressDialog;

    private List<Product> mProductsList = new ArrayList<>();
    private List<OrderBean> mOrdersList = new ArrayList<>();

    private final int TYPE_ERROR = -1;

    private final int TYPE_INITIALIZE = 0;

    private final int TYPE_LOGIN = 1;

    private final int TYPE_REWARD = 2;

    private final int TYPE_VERIFY = 3;

    private final int TYPE_QUERY_PAGE_PRODUCT = 4;

    private final int TYPE_QUERY_PRODUCT = 5;

    private final int TYPE_QUERY_RECORD = 6;

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
                    displayResult(TYPE_INITIALIZE, response);
                }

                @Override
                public void onError(String error) {
                    displayResult(TYPE_ERROR, error);
                }
            }, this);

        });
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            showProgress();
            QooAppOpenSDK.getInstance().login(new QooAppCallback() {
                @Override
                public void onSuccess(String response) {
                    displayResult(TYPE_INITIALIZE, response);
                }

                @Override
                public void onError(String error) {
                    displayResult(TYPE_ERROR, error);
                }
            }, MainActivity.this);

        });
        findViewById(R.id.btn_last_version).setOnClickListener(v -> {
            showProgress();
            QooAppOpenSDK.getInstance().latestVersionCode(new QooAppCallback() {
                @Override
                public void onSuccess(String response) {
                    displayResult(TYPE_ERROR, response);
                }

                @Override
                public void onError(String error) {
                    displayResult(TYPE_ERROR, error);
                }
            });

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
        findViewById(R.id.btn_open_qoo).setOnClickListener(v -> {
            QooAppOpenSDK.getInstance().openGameDetail(this);
//            or QooAppOpenSDK.getInstance().openGameDetail(this, "action=download");
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

        findViewById(R.id.btn_product_page).setOnClickListener(v -> {
            showProgress();
            QooAppOpenSDK.getInstance().queryProducts(new QooAppCallback() {
                @Override
                public void onSuccess(String response) {
                    hideProgress();
                    displayResult(TYPE_QUERY_PAGE_PRODUCT, response);
                }

                @Override
                public void onError(String error) {
                    displayResult(TYPE_ERROR, error);
                }
            }, 1);
        });

        findViewById(R.id.btn_products).setOnClickListener(v -> {
            showProgress();
            QooAppOpenSDK.getInstance().queryProducts(new QooAppCallback() {
                @Override
                public void onSuccess(String response) {
                    displayResult(TYPE_QUERY_PRODUCT, response);
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
                public void onSuccess(String response) {
                    hideProgress();
                    displayResult(TYPE_QUERY_RECORD, response);
                }

                @Override
                public void onError(String error) {
                    hideProgress();
                    displayResult(TYPE_ERROR, error);
                }
            });
        });
        mEdtLanguage = findViewById(R.id.edt_locale);
        findViewById(R.id.btn_select_locale).setOnClickListener(v -> {
            final String[] locales = {SDKLocales.EN, SDKLocales.JA, SDKLocales.KO, SDKLocales.ES, SDKLocales.TH, SDKLocales.FR, SDKLocales.PT, SDKLocales.ZH_CN, SDKLocales.ZH_HK, SDKLocales.ZH_TW};
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("chose language")
                            .setSingleChoiceItems(locales, 0, (dialog, which) -> {
                                mEdtLanguage.setText(locales[which]);
                                dialog.dismiss();
                            })
                    .setNegativeButton("", null)
                    .show();
        });
        findViewById(R.id.btn_set_locale).setOnClickListener(v -> {
            boolean isSuccess = QooAppOpenSDK.setLocale(mEdtLanguage.getText().toString());
            showToast("Set language: "+ mEdtLanguage.getText().toString() +", isSuccess" + isSuccess);
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
                case TYPE_INITIALIZE:
                    showToast("initialize success");
                    break;
                case TYPE_LOGIN:
                    showFunctionView();
                    break;
                case TYPE_QUERY_PAGE_PRODUCT:
                    parsePageProducts(result);
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
     * parse products
     *
     * @param response
     */
    private void parsePageProducts(String response) {
        mProductsList.clear();
        Gson gson = new Gson();
        PageProductResponse.DataBean dataBean = gson.fromJson(getDataStringFromResponse(response), PageProductResponse.DataBean.class);
        PageProductResponse.PagerBean pagerBean = dataBean.getPager();
        mProductsList = dataBean.getItems();
        if (pagerBean.getTotal() < pagerBean.getSize()) {
            showToast("only "+pagerBean.getTotal()+" products, no need get next page");
        } else {
            showToast("has more products, this is No. "+pagerBean.getPage() +" page, "+pagerBean.getSize()+" products per page. There are "+pagerBean.getTotal()+" products in total");
        }
        showProductView();
    }

    /**
     * parse products list info
     * @param response
     */
    private void parseProducts(String response) {
        mProductsList.clear();
        mProductsList = parseString2List(getDataStringFromResponse(response), Product.class);
        showProductView();
    }

    private void showProductView() {
        ProductAdapter adapter = new ProductAdapter(this, mProductsList, (type, productId) -> {
            if (type == ProductAdapter.TYPE_DETAIL) {
                // get product detail
                showProgress();
                QooAppOpenSDK.getInstance().queryProductsInfo(new QooAppCallback() {
                    @Override
                    public void onSuccess(String result) {
                        hideProgress();
                        displayResult(TYPE_ERROR, result);
                    }

                    @Override
                    public void onError(String error) {
                        displayResult(TYPE_ERROR, error);
                    }
                }, productId);
            } else {
                // buy
                QooAppOpenSDK.getInstance().purchase(new PaymentCallback() {
                    @Override
                    public void onComplete(String response) {

                        //Handle success case
                        /**
                         * 1、purchase success，then please distribute goods to player.
                         * 2、When player get product, you must call consumePurchase();
                         */
                        Gson gson = new Gson();
                        OrderBean orderBean = gson.fromJson(getDataStringFromResponse(response), OrderBean.class);
                        showToast("Purchasing successful，Consuming...[purchase_id:" + orderBean.getPurchase_id() + ",token:" + orderBean.getToken());
                        showPaymentDialog((dialog, which) -> {
                            showProgress();
                            consumePurchase(orderBean.getPurchase_id(), orderBean.getToken());
                        });
                    }

                    @Override
                    public void onError(String error) {
                        showToast("Error:" + error);
                    }

                    @Override
                    public void onCancel() {
                        showToast("Be canceled");
                    }
                }, MainActivity.this,  productId, "your cpOrderId:"+System.currentTimeMillis(), "your developerPayload:"+System.currentTimeMillis());
            }
        });
        mListView.setVisibility(View.VISIBLE);
        mListView.setAdapter(adapter);
        showProducts("Products");
    }

    /**
     * parse records list
     * @param response
     */
    private void parseRecords(String response) {
        mOrdersList = parseString2List(getDataStringFromResponse(response), OrderBean.class);
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

    /**
     * get data json string from response.
     * You can use `QooAppOpenSDK.getDataFromResponse(response)` to replace the following method
     * @param response
     * @return
     */
    private String getDataStringFromResponse(String response) {
//        return QooAppOpenSDK.getDataFromResponse(response);
        try {
            JSONObject resJson = new JSONObject(response);
            String dataString = resJson.optString("data");
            return new String(Base64.decode(dataString, DEFAULT), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}