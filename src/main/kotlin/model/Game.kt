package model

import org.eclipse.jetty.websocket.api.Session
import java.util.HashMap

class Game(
    val targets: Collection<Target>,
    cursors: HashMap<Session, Cursor>,
    session: Session? = null
) {
    val cursors: Collection<Cursor> = cursors.filterKeys { session !== it }.values
}
