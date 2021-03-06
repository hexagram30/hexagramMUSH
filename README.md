# hexagramMUSH

*A JVM (Clojure) MUSH platform, built on a graph database*

[![][logo]][logo-large]


#### Contents

* [Dependencies](#dependencies-)
* [Startup](#startup-)
* [Connecting](#connecting-)
* [Usage](#usage-)
   * [Registration Mode](#registration-mode-)
   * [Player Mode](#player-mode-)
   * [Builder Mode](#builder-mode-)
   * [Multi-Realm Mode](#multi-realm-mode-)
* [Background](#background-)
* [Donating](#donating-)
* [License](#license-)

This project is in pause while the subprojects upon which it depends are
being constructed. To see the progress on those, visit the
[hexagram30 Github org](https://github.com/hexagram30).


## Dependencies [&#x219F;](#contents)

* Java
* `lein`
* Docker


## Startup [&#x219F;](#contents)

1. Start the world's graph database: `lein with-profile +redis-plugin start-graphdb`
1. Start the game's key store: `lein with-profile +redis-plugin start-db`
1. Ensure they are both running: `docker ps`
1. Start the MUSH:
   * From the REPL: `lein with-profile +redis-plugin repl` and then `(startup)`
   * From the CLI: `lein with-profile redis-plugin start-mush`


## Connecting [&#x219F;](#contents)

Telnet: `rlwrap telnet localhost 1130`

Secure Telnet: `rlwrap ncat --telnet --ssl -n 127.0.0.1 1122`

If you are on Mac OS X and have installed netcat via Homebrew, the `--ssl`
option won't be available to you; instead, you should use the following:

```
openssl s_client -connect 127.0.0.1:1122
```

## Usage [&#x219F;](#contents)


### Registration Mode [&#x219F;](#contents)

TBD


### Player Mode [&#x219F;](#contents)

TBD


### Builder Mode [&#x219F;](#contents)

TBD


### Multi-Realm Mode [&#x219F;](#contents)

TBD


## Background [&#x219F;](#contents)

This project has been many, many years in the making, almost since my first
playing of a text adventure game on a CP\M machine in 1981. Over the years,
I have made several attempts in several different languages. For the past
ten years or so, all of my plans were based on my experience with
[PennMUSH][pennmush] of the TinyMUD lineage.

As wonderful as PennMUSH is, thera re many thing about it which I would
change, mostly from an infrastructure and organization perspective. The
keystone for this planning work, though, has been my desire for a MUSH
implementation to be a fully distributed system, capable of not only
supporting multiple playing instances, but multiple backing databases
as well.

To make things more difficult, I wanted a database that perfecly matched the
usage of connection-based world-creation. As such, I eventually turned my eye
to Graph datbases, and settled on [OrientDB][orientdb] (and a corresponding
[Clojure library][clj-odbp] by [7bridges][7bridges]). With this in hand, the
project finally started to gain steam and the component architecutre started
to take shape. (Note, however, that I later switched to using Redis -- both
as the backend for a graph DB as well as a key store.)

The next thing needed, before coding on this iteration could commense, was a
name. Here's what we had:
* OrientDB - East, sunrise, dawn, compass
* 7bridges' clj-odbp - seven, bridges

Hey, there's a pattern! **8** (compass points), **7** bridages, **6** ... what?
Lines, I guess :-) The eight trigrams of the bāguà (八卦) were already in my
head as possible inspiration for a name, so it was a small leap to six lines
for a hexagram of the Zhōu yì (周易, also known as the [Yijing][yijing]).
The next step was looking at the various translations of the 64 hexagrams of
the Zhōu yì to find one that fit with the spirit of building good games for
all.

Eventually, I settled on ䷝, hexagram 30 - lí (離):

* Radiance
* Clarity
* The Net
* The Clinging
* Fire
* Associated with the plumage of the phoenix

From several translations, I cobbled this together, tied it back to the
underlying database, and set it as the Gihub org's description:

*The sun rises, an image of fire. A net of radiance illumines the eight points: clarity.*

Let the world-building commence ...


## Donating [&#x219F;](#contents)

A donation account for supporting development on this project has been set up
on Liberapay here:

* [https://liberapay.com/hexagram30/donate](https://liberapay.com/hexagram30/donate)

You can learn more about Liberapay on its [Wikipedia entry][libera-wiki] or on the
service's ["About" page][libera-about].

[libera-wiki]: https://en.wikipedia.org/wiki/Liberapay
[libera-about]: https://liberapay.com/about/


## License [&#x219F;](#contents)

Copyright © 2018, Hexagram30

Apache License, Version 2.0


<!-- Named page links below: /-->

[logo]: https://raw.githubusercontent.com/hexagram30/resources/master/branding/logo/hmush-logo-2-long-with-text-x811.png
[logo-large]: https://raw.githubusercontent.com/hexagram30/resources/master/branding/logo/hmush-logo-2-long-with-text-x4030.png
[clojure]: http://clojure.org/
[clojure-logo]: https://raw.githubusercontent.com/hexagram30/resources/master/images/logos/clojure.png
[orientdb-logo]: https://raw.githubusercontent.com/hexagram30/resources/master/images/logos/orientdb.png
[orientdb]: https://orientdb.com/
[7bridges]: https://7bridges.eu/
[7bridges-logo]: https://raw.githubusercontent.com/hexagram30/resources/master/images/logos/7bridges.png
[clj-odbp]: https://github.com/7bridges-eu/clj-odbp
[pennmush]: https://github.com/pennmush]
[yijing]: https://en.wikipedia.org/wiki/I_Ching
