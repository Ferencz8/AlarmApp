package countingsheep.alarm.db.repositories.tasks.paymentdetails;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.PaymentDetailsDao;
import countingsheep.alarm.db.entities.PaymentDetails;

public class InsertPaymentDetailsTask extends AsyncTask<Void, Void, Long> {

    private WeakReference<PaymentDetailsDao> paymentDetailsDaoWeakReference;
    private WeakReference<OnAsyncResponse<Long>> onAsyncResponseWeakReference;
    private PaymentDetails paymentDetails;

    public InsertPaymentDetailsTask(PaymentDetailsDao paymentDetailsDao,
                           PaymentDetails paymentDetails)
    {
        this(paymentDetailsDao, paymentDetails, null);
    }


    public InsertPaymentDetailsTask(PaymentDetailsDao paymentDetailsDao,
                           PaymentDetails paymentDetails,
                           OnAsyncResponse<Long> onAsyncResponse) {
        this.paymentDetailsDaoWeakReference = new WeakReference<>(paymentDetailsDao);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
        this.paymentDetails = paymentDetails;
    }

    @Override
    protected Long doInBackground(Void... voids) {

        PaymentDetailsDao paymentDetailsDao = paymentDetailsDaoWeakReference.get();

        if(paymentDetailsDao==null)
            return null;

        long paymentDetailsId = paymentDetailsDao.insert(paymentDetails);
        return paymentDetailsId;
    }

    @Override
    protected void onPostExecute(Long result) {

        OnAsyncResponse<Long> onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if(onAsyncResponse!=null) {
            onAsyncResponse.processResponse(result);
        }
    }
}
