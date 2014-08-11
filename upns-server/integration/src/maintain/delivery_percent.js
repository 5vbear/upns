/**
 * Created by pippo on 14-7-6.
 */

/*step 1*/
db.runCommand({
    'mapReduce': 'acks',
    'map': function () {
        /*由于mongo对于map后只有一条的记录不会调用reduce(私信就是这样)*/
        /*所以map也要输出和reduce一样的数据结构*/
        var out = {'messageId': this.m,
            'total': 1,
            'reached': this.k ? 1 : 0,
            'percent': this.k ? 1 : 0,
            'type': 'private',
            'unreached_user': this.k ? [] : [this.u]
        };
        emit(this.m, out);
    },
    'reduce': function (messageId, acks) {
        var reached = 0;
        var unreached_user = [];
        for (var i = 0; i < acks.length; i++) {
            if (acks[i].reached == 1) {
                reached++;
            } else {
                unreached_user = unreached_user.concat(acks[i].unreached_user);
            }
        }
        return {'messageId': messageId,
            'total': acks.length,
            'reached': reached,
            'percent': reached / acks.length,
            'type': 'group',
            'unreached_user': unreached_user
        };
    },
    'out': {'replace': 'deliver_percent'},
    'query': {'ct': {$gte: (new Date().getTime() - 1000 * 60 * 60 * 24 * 7), $lte: (new Date().getTime())}}
});

/*step 2*/
function cal_private_percent() {
    var items = db.deliver_percent.find({'value.type': 'private'});
    var percent = 0;
    var total = 0;
    items.forEach(function (item) {
        percent += item.value.percent;
        total++;
    });
    return {'type': 'private', 'percent': percent / total, 'total': total};
}

function cal_group_percent() {
    var items = db.deliver_percent.find({'value.type': 'group'});
    var reached = 0;
    var total = 0;
    items.forEach(function (item) {
        reached += item.value.reached;
        total += item.value.total;
    });
    return {'type': 'group', 'total': total, 'reached': reached, 'percent': reached / total};
}

/*step 3*/
cal_private_percent();
cal_group_percent();

/*step 4*/
///opt/mongodb/bin/mongoexport -d upns_db -c deliver_percent -o deliver_percent.json