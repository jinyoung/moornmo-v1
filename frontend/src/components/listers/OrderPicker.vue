<template>
    <div>
        <v-list two-line v-if="list.length > 0">
            <v-list-item-group 
                    v-model="selected" 
                    color="indigo"
                    @change="select"
            >
                <v-list-item v-for="(item, idx) in list" :key="idx">
                    <template v-slot:default="{ active }">
                        <v-list-item-avatar color="grey darken-1">
                        </v-list-item-avatar>
                        
                        <v-list-item-content>
                            <v-list-item-title>
                            </v-list-item-title>
                            <v-list-item-subtitle>
                                AccountDate :  {{item.accountDate }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                Type :  {{item.type }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                Number :  {{item.number }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                Client :  {{item.client }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                OrderDate :  {{item.orderDate }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                DeliveryDate :  {{item.deliveryDate }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                Manager :  {{item.manager }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                RegisterName :  {{item.registerName }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                OrderItem :  {{item.orderItem }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                OrderCount :  {{item.orderCount }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                Cost :  {{item.cost }}
                            </v-list-item-subtitle>
                            <v-list-item-subtitle>
                                Memo :  {{item.memo }}
                            </v-list-item-subtitle>
                        </v-list-item-content>

                        <v-list-item-action>
                            <v-checkbox :input-value="active" color="indigo"></v-checkbox>
                        </v-list-item-action>
                    </template>
                </v-list-item>
            </v-list-item-group>
        </v-list>
    </div>
</template>


<script>
    const axios = require('axios').default;

    export default {
        name: 'OrderPicker',
        props: {
            value: [String, Object, Array, Number, Boolean],
        },
        data: () => ({
            list: [],
            selected: null,
        }),
        async created() {
            var me = this;
            var temp = await axios.get(axios.fixUrl('/orders'))
            if(temp.data) {
                me.list = temp.data._embedded.orders;
            }

            if(me.value && typeof me.value == "object" && Object.values(me.value)[0]) {
                var id = Object.values(me.value)[0];
                var tmpValue = await axios.get(axios.fixUrl('/orders/' + id))
                if(tmpValue.data) {
                    var val = tmpValue.data
                    me.list.forEach(function(item, idx) {
                        if(item.name == val.name) {
                            me.selected = idx
                        }
                    })
                }
            }
        },
        methods: {
            select(val) {
                var obj = {}
                if(val != undefined) {
                    var arr = this.list[val]._links.self.href.split('/');
                    obj['id'] = arr[4]; 
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    this.$emit('selected', obj);
                }
            },
        },
    };
</script>

