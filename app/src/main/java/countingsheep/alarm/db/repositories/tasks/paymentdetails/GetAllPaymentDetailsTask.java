package countingsheep.alarm.db.repositories.tasks.paymentdetails;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.db.dao.PaymentDetailsDao;
import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;

public class GetAllPaymentDetailsTask extends AsyncTask<Void, Void, List<PaymentDetails>> {

    private WeakReference<PaymentDetailsDao> paymentDetailsDaoWeakReference;
    private WeakReference<OnAsyncResponse<List<PaymentDetails>>> onAsyncResponseWeakReference;
    private PaymentStatus paymentStatus;

    public GetAllPaymentDetailsTask(PaymentDetailsDao paymentDetailsDao, PaymentStatus paymentStatus)
    {
        this(paymentDetailsDao,paymentStatus,  null);
    }


    public GetAllPaymentDetailsTask(PaymentDetailsDao paymentDetailsDao,
                                    PaymentStatus paymentStatus,
                                    OnAsyncResponse<List<PaymentDetails>> onAsyncResponse) {
        this.paymentDetailsDaoWeakReference = new WeakReference<>(paymentDetailsDao);
        this.onAsyncResponseWeakReference = new WeakReference<>(onAsyncResponse);
        this.paymentStatus = paymentStatus;
    }

    @Override
    protected List<PaymentDetails> doInBackground(Void... voids) {

        PaymentDetailsDao paymentDetailsDao = paymentDetailsDaoWeakReference.get();

        if(paymentDetailsDao==null)
            return null;

        List<PaymentDetails> paymentDetails = paymentDetailsDao.getAll(this.paymentStatus.getCode());
        return paymentDetails;
    }

    @Override
    protected void onPostExecute(List<PaymentDetails> result) {

        OnAsyncResponse<List<PaymentDetails>> onAsyncResponse = this.onAsyncResponseWeakReference.get();
        if(onAsyncResponse!=null) {
            onAsyncResponse.processResponse(result);
        }
    }
}