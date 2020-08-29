package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import lombok.Builder;
import vn.zalopay.phucvt.fooapp.common.mapper.EntityMapper;
import vn.zalopay.phucvt.fooapp.model.Participant;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
public class ParticipantDAImpl extends BaseTransactionDA implements ParticipantDA{

    private final DataSource dataSource;
    private final AsyncHandler asyncHandler;

    private static final String GET_CONVERSATION_ID_BY_USER_ID =
            "SELECT * FROM participants WHERE user_id = ?";

    @Override
    public Future<Set<String>> getConversationIdByUserId(String userId) {
        Future<Set<String>> future = Future.future();
        asyncHandler.run(
                () -> {
                    Object[] params = {userId};
                    queryEntity(
                            "queryParticipants",
                            future,
                            GET_CONVERSATION_ID_BY_USER_ID,
                            params,
                            this::mapRs2EntityParticipant,
                            dataSource::getConnection,
                            false);
                });

        return future;
    }

    private Set<String> mapRs2EntityParticipant(ResultSet resultSet) throws Exception {
        Participant p = null;
        Set<String> res = new HashSet<>();
        while (resultSet.next()) {
            p = new Participant();
            EntityMapper.getInstance().loadResultSetIntoObject(resultSet, p);
            res.add(p.getConversation_id());
        }

        return res;
    }
}
