<template>

    <v-data-table
        :headers="headers"
        :items="orderSearch"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'OrderSearchView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            orderSearch : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/orderSearches'))

            temp.data._embedded.orderSearches.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.orderSearch = temp.data._embedded.orderSearches;
        },
        methods: {
        }
    }
</script>

