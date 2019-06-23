package countingsheep.alarm.db.repositories.tasks.paymentdetails;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.dao.PaymentDetailsDao;
import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;

public class GetSumAmountTask extends AsyncTask<Void, Void, Integer> {

    private WeakReference<PaymentDetailsDao> paymentDetailsDaoWeakReference;
    private WeakReference<OnAsyncResponse<Integer>> onAsyncResponseWeakReference;

    public GetSumAmountTask(PaymentDetailsDao paymentDetailsDao)
    {
        this(paymentDetailsDao,  null);
    }


    public GetSumAmountTask(PaymentDetailsDao paymentDetailsDao,
                                    OnAsyncResponse<Integer> onAsyncResponse) {
        this.paymentDetailsDaoWeakReference = new WeakReference<>(paymentDetailsDao);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        PaymentDetailsDao paymentDetailsDao = paymentDetailsDaoWeakReference.get();

        if(paymentDetailsDao==null)
            return null;

        Integer amount = paymentDetailsDao.getAllAmount();
        return amount;
    }

    @Override
    protected void onPostExecute(Integer result) {
        OnAsyncResponse<Integer> onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if(onAsyncResponse!=null) {
            onAsyncResponse.processResponse(result);
        }
    }
}
