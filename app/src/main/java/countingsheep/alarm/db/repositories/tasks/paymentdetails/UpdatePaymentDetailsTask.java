package countingsheep.alarm.db.repositories.tasks.paymentdetails;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import countingsheep.alarm.db.dao.PaymentDetailsDao;
import countingsheep.alarm.db.entities.PaymentDetails;

public class UpdatePaymentDetailsTask  extends AsyncTask<Void, Void, Void> {

    private WeakReference<PaymentDetailsDao> paymentDetailsDaoWeakReference;
    private PaymentDetails paymentDetails;


    public UpdatePaymentDetailsTask(PaymentDetailsDao paymentDetailsDao,
                                    PaymentDetails paymentDetails) {
        this.paymentDetailsDaoWeakReference = new WeakReference<>(paymentDetailsDao);
        this.paymentDetails = paymentDetails;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        PaymentDetailsDao paymentDetailsDao = paymentDetailsDaoWeakReference.get();

        if(paymentDetailsDao!=null) {
            paymentDetailsDao.update(paymentDetails);
        }
        return null;
    }
}
