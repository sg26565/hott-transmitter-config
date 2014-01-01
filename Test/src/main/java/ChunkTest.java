import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Channel;
import model.Transmitter;

import com.x5.template.Chunk;
import com.x5.template.Theme;

public class ChunkTest {
  public static void main(final String[] args) throws IOException {
    final Theme theme = new Theme();
    final Chunk c = theme.makeChunk("test");

    c.set("name", "John Doe");

    final Transmitter t = new Transmitter();
    t.setVendor("Graupner/SJ");
    t.setName("MC-32");
    t.setVersion("1.073");
    t.setCamelCaseString("something");

    final List<Channel> channels = new ArrayList<Channel>();
    t.setChannels(channels);

    final Random random = new Random(System.currentTimeMillis());
    for (int i = 0; i < 16; i++) {
      final Channel channel = new Channel();
      channels.add(channel);
      channel.setNumber(i);
      channel.setReverse(random.nextBoolean());
      channel.setTravelLow(-1 * random.nextInt(101));
      channel.setTravelHigh(random.nextInt(101));
      channel.setLimitLow(-1 * random.nextInt(151));
      channel.setLimitHigh(random.nextInt(151));
    }

    t.setFooStrings(new String[] { "one", "two", "three" });
    c.setToBean("transmitter", t);

    c.render(System.out);
  }
}
