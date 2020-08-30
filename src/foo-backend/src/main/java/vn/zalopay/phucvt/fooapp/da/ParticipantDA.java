package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.Participant;

import java.util.Set;

public interface ParticipantDA {
    Future<Set<String>> getConversationIdByUserId(String userId);
}
