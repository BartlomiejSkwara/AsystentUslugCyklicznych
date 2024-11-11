<template>
  <div>
    <h1>Upload Certificate from File</h1>
    <form @submit.prevent="uploadFile">
      <div>
        <label for="file">Select file:</label>
        <input type="file" id="file" @change="handleFileUpload" required />
      </div>
      <button type="submit">Upload</button>
      <button class="btn1" @click="goBack">Back</button>
    </form>
  </div>
</template>

<script>
import { fetchWrapper } from '@/utility';
export default {
  data() {
    return {
      file: null
    };
  },
  methods: {
    handleFileUpload(event) {
      this.file = event.target.files[0];
    },
    async uploadFile() {
      if (!this.file) return;

      const formData = new FormData();
      formData.append('file', this.file);

      try {
        const response = await fetchWrapper(this,'/api/certificate/upload', {
          method: 'POST',
          body: formData
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        this.$router.push('/certificates');
      } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
      }
    },
    goBack() {
      this.$router.push('/certificates');
    }
  }
};
</script>

<style src="@/assets/style.css"></style>