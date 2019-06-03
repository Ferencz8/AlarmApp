package countingsheep.alarm.db.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import countingsheep.alarm.core.contracts.data.OnAsyncResponse;
import countingsheep.alarm.core.contracts.data.PaymentDetailsRepository;
import countingsheep.alarm.db.AlarmDatabase;
import countingsheep.alarm.db.dao.PaymentDetailsDao;
import countingsheep.alarm.db.entities.PaymentDetails;
import countingsheep.alarm.db.entities.PaymentStatus;
import countingsheep.alarm.db.repositories.tasks.alarm.GetGenericTask;
import countingsheep.alarm.db.repositories.tasks.paymentdetails.GetAllPaymentDetailsTask;
import countingsheep.alarm.db.repositories.tasks.paymentdetails.InsertPaymentDetailsTask;
import countingsheep.alarm.db.repositories.tasks.paymentdetails.UpdatePaymentDetailsTask;

@Singleton
public class PaymentDetailsRepositoryImpl implements PaymentDetailsRepository {

    private PaymentDetailsDao dao;

    @Inject
    public PaymentDetailsRepositoryImpl(AlarmDatabase alarmDatabase) {
        this.dao = alarmDatabase.paymentDao();
    }

    @Override
    public void insert(PaymentDetails paymentDetails) {
        new InsertPaymentDetailsTask(dao, paymentDetails).execute();
    }

    @Override
    public void update(PaymentDetails paymentDetails) {
        new UpdatePaymentDetailsTask(dao, paymentDetails).execute();
    }

    @Override
    public void getAll(PaymentStatus paymentStatus, OnAsyncResponse<List<PaymentDetails>> onAsyncResponse) {
        new GetAllPaymentDetailsTask(this.dao, paymentStatus, onAsyncResponse).execute();
    }

    @Override
    public void getForAlarmReactionId(int alarmReactionId, OnAsyncResponse<PaymentDetails> onAsyncResponse) {
        new GetGenericTask<PaymentDetailsDao, PaymentDetails>(dao, new GetGenericTask.OnTaskHandler<PaymentDetailsDao, PaymentDetails>() {
            @Override
            public PaymentDetails doInBackground(PaymentDetailsDao dao) {
                return dao.getForAlarmReactionId(alarmReactionId);
            }

            @Override
            public void onPostExecute(PaymentDetails returnedValues) {
                onAsyncResponse.processResponse(returnedValues);
            }
        }).execute();
    }
}
