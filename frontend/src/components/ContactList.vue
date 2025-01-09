<template>
  <div id="contacts">
    <ul>
      <li v-for="contact in contacts" :key="contact" :class="{ active: contact === currentRecipient }"
        @click="select(contact)">
        <span>{{ contact }}</span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'ContactList',
  props: {
    username: String,
    currentRecipient: String,
    contacts: Array
  },
  methods: {
    select(contact) {
      this.$emit('selectRecipient', contact)
    }
  }
}
</script>

<style scoped>
#contacts {
  width: 250px;
  background-color: rgba(44, 62, 80, 0.9);
  color: #ecf0f1;
  max-height: 600px;
  overflow-y: auto;
}

#contacts ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

#contacts li {
  padding: 15px 20px;
  cursor: pointer;
  border-bottom: 1px solid #34495e;
  display: flex;
  align-items: center;
  transition: background-color 0.3s;
}

#contacts li:hover,
#contacts li.active {
  background-color: #34495e;
}
</style>
